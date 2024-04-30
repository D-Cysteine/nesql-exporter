package com.github.dcysteine.nesql.exporter.render;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.guihook.GuiContainerManager;
import com.github.dcysteine.nesql.exporter.main.Logger;
import com.github.dcysteine.nesql.exporter.main.config.ConfigOptions;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL30;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Consumer;

/** Singleton class that handles rendering items and fluids and saving the resulting image data. */
public enum Renderer {
    INSTANCE;

    public static final String IMAGE_FILE_EXTENSION = ".png";
    private static final String IMAGE_FORMAT = "PNG";

    /** Enum which handles rendering different types / sizes of images. */
    public enum RenderTarget {
        ICON,
        MOB,
        ;

        /** Holds the currently bound render target, or null if all render targets are unbound. */
        private static RenderTarget boundBuffer = null;

        private static void forEach(Consumer<RenderTarget> consumer) {
            Arrays.stream(values()).forEach(consumer);
        }

        private int imageDim;
        private Framebuffer framebuffer;

        /** Initializes the framebuffer, which must be done on the render thread. */
        private void initialize() {
            framebuffer = new Framebuffer(imageDim, imageDim, true);
        }

        /** Binds the framebuffer if not already bound. Must be done on the render thread. */
        private void bind() {
            if (boundBuffer != this) {
                framebuffer.bindFramebuffer(true);
                // Do we need to bind GL_DRAW_FRAMEBUFFER here as well?
                // Seems to work fine as-is though...
                OpenGlHelper.func_153171_g(GL30.GL_READ_FRAMEBUFFER, framebuffer.framebufferObject);

                boundBuffer = this;
            }
        }

        /** Unbinds the framebuffer if bound. Must be done on the render thread. */
        private void unbind() {
            if (boundBuffer == this) {
                framebuffer.unbindFramebuffer();
                boundBuffer = null;
            }
        }

        /** Destroys the framebuffer, which must be done on the render thread. */
        private void destroy() {
            framebuffer.deleteFramebuffer();
            framebuffer = null;
        }
    }

    private File imageDirectory;

    // Used for intermittent logging.
    private int loggingCounter;

    /**
     * This method is meant to be called from the client thread, prior to setting the dispatcher
     * state to {@code INITIALIZING}. It performs initialization of non-render-related variables.
     */
    public void preInitialize(File imageDirectory) {
        RenderTarget.ICON.imageDim = ConfigOptions.ICON_DIMENSION.get();
        RenderTarget.MOB.imageDim = ConfigOptions.MOB_IMAGE_DIMENSION.get();

        this.imageDirectory = imageDirectory;
        this.loggingCounter = 0;
    }

    /**
     * Performs initialization of render-related variables, which must be done on the render thread.
     */
    private void initialize() {
        RenderTarget.forEach(RenderTarget::initialize);

        RenderDispatcher.INSTANCE.setRendererState(RenderDispatcher.RendererState.INITIALIZED);
    }

    /**
     * Performs destruction of render-related variables, which must be done on the render thread.
     */
    private void destroy() {
        RenderTarget.forEach(RenderTarget::destroy);

        RenderDispatcher.INSTANCE.setRendererState(RenderDispatcher.RendererState.UNINITIALIZED);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    @SuppressWarnings("unused")
    public void onRender(TickEvent.RenderTickEvent event) {
        switch (RenderDispatcher.INSTANCE.getRendererState()) {
            case UNINITIALIZED:
            case ERROR:
                return;

            case INITIALIZING:
                initialize();
                return;

            case INITIALIZED:
                break;

            case DESTROYING:
                destroy();
                return;

            default:
                throw new IllegalArgumentException(
                        "Unrecognized render state: "
                                + RenderDispatcher.INSTANCE.getRendererState());
        }

        if (RenderDispatcher.INSTANCE.noJobsRemaining()) {
            RenderDispatcher.INSTANCE.notifyJobsCompleted();
            return;
        }

        if (Logger.intermittentLog(++loggingCounter)) {
            Logger.MOD.info("Remaining render jobs: {}", RenderDispatcher.INSTANCE.getJobCount());
            loggingCounter = 0;
        }

        setupRenderState();
        try {
            for (int i = 0; i < ConfigOptions.RENDER_IMAGES_PER_TICK.get(); i++) {
                Optional<RenderJob> jobOptional = RenderDispatcher.INSTANCE.getJob();
                if (!jobOptional.isPresent()) {
                    break;
                }

                RenderJob job = jobOptional.get();
                RenderTarget target = job.getRenderTarget();
                bindAndClearBuffer(target);
                render(job);
                BufferedImage image = readImage(target);

                File outputFile = new File(imageDirectory, job.getImageFilePath());
                // Not sure why, but this check fails spuriously every now and then.
                // It complains that the file exists, but I checked and it didn't actually exist.
                // Let's just... ignore it for now XD
                // The failures might be due to Windows getting confused by '~' in filenames XS
                /*
                if (outputFile.exists()) {
                    // If we cannot avoid queueing up duplicate render jobs, we can replace this
                    // throw with a continue, and move this check to before we call readImage(...)
                    throw new RuntimeException(
                            "Render output file already exists: " + outputFile.getPath());
                }
                 */

                File parentDir = outputFile.getParentFile();
                if (parentDir.exists() && !parentDir.isDirectory()) {
                    throw new RuntimeException(
                            "Render output file directory already exists as a file: "
                                    + parentDir.getPath());
                } else if (!parentDir.exists() && !parentDir.mkdirs()) {
                    throw new RuntimeException(
                            "Could not create render output file directory: "
                                    + parentDir.getPath());
                }

                try {
                    ImageIO.write(image, IMAGE_FORMAT, outputFile);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } finally {
            teardownRenderState();
        }
    }

    /** Returns whether the render succeeded. */
    private void render(RenderJob job) {
        RenderTarget target = job.getRenderTarget();

        switch (job.getType()) {
            case ITEM:
                GuiContainerManager.drawItem(0, 0, job.getItem());
                break;

            case FLUID:
                FluidStack fluidStack = job.getFluid();
                IIcon icon = fluidStack.getFluid().getIcon(fluidStack);
                // Some fluids don't set their icon colour, so we have to blend in the colour.
                int colour = fluidStack.getFluid().getColor(fluidStack);
                GL11.glColor3ub(
                        (byte) ((colour & 0xFF0000) >> 16),
                        (byte) ((colour & 0x00FF00) >> 8),
                        (byte) (colour & 0x0000FF));

                GuiDraw.changeTexture(TextureMap.locationBlocksTexture);
                GuiDraw.gui.drawTexturedModelRectFromIcon(0, 0, icon, 16, 16);

                // Reset colour blending.
                GL11.glColor4f(1f, 1f, 1f, 1f);
                break;

            case MOB:
                Optional<Entity> entityOptional = job.getMob().createEntity();
                if (!entityOptional.isPresent()) {
                    // This shouldn't ever happen,
                    // because we already check for null entity before enqueueing the render job.
                    Logger.MOD.error("Could not create entity: {}", job);
                    break;
                }

                Entity entity = entityOptional.get();
                float scale =
                        // Magic constant to make most mobs fit within the rendered image.
                        // It's not a perfect fit, and varies a bit from mob to mob.
                        // TODO maybe improve this? Probably pretty difficult, though.
                        // Could also make it a config option.
                        0.035f * Math.min(
                                target.imageDim / entity.width,
                                target.imageDim / entity.height);

                // Lots of mob renderers don't do a good job of cleaning up their state...
                GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
                GL11.glPushMatrix();
                GL11.glTranslatef(8f, 0f, 0f);  // Center the mob in the rendered image.
                GL11.glScalef(scale, scale, scale);
                RenderManager.instance.renderEntityWithPosYaw(entity, 0d, 0d, 0d, 0f, 1f);
                GL11.glPopMatrix();
                GL11.glPopAttrib();
                break;

            default:
                throw new IllegalArgumentException("Unrecognized job type: " + job);
        }
    }

    /** Returns the rendered image, in {@link BufferedImage#TYPE_INT_ARGB} format. */
    private BufferedImage readImage(RenderTarget target) {
        int imageDim = target.imageDim;
        ByteBuffer imageByteBuffer = BufferUtils.createByteBuffer(4 * imageDim * imageDim);
        GL11.glReadPixels(
                0, 0, imageDim, imageDim, GL12.GL_BGRA, GL11.GL_UNSIGNED_BYTE, imageByteBuffer);

        int[] pixels = new int[imageDim * imageDim];
        imageByteBuffer.asIntBuffer().get(pixels);

        // OpenGL uses inverted y-coordinates compared to our draw methods.
        // So we must flip the saved image vertically.
        //
        // Unfortunately, for some reason, the rendering seems to break if we try to invert using
        // OpenGL matrix transforms, so let's just do this on the pixel array.
        //
        // For some reason, mob rendering doesn't need to be flipped.
        if (target == RenderTarget.ICON) {
            int[] flippedPixels = new int[pixels.length];
            for (int i = 0; i < pixels.length; i++) {
                int x = i % imageDim;
                int y = imageDim - (i / imageDim + 1);
                flippedPixels[i] = pixels[x + imageDim * y];
            }
            pixels = flippedPixels;
        }

        BufferedImage image =
                new BufferedImage(imageDim, imageDim, BufferedImage.TYPE_INT_ARGB);
        image.setRGB(0, 0, imageDim, imageDim, pixels, 0, imageDim);
        return image;
    }

    private void bindAndClearBuffer(RenderTarget target) {
        target.bind();

        // Parameters are RGBA. Set full transparent background.
        GL11.glClearColor(0f, 0f, 0f, 0f);
        GL11.glClearDepth(1D);
        GL11.glClear(16384 | 256);
    }

    private void setupRenderState() {
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0, 1.0, 1.0, 0.0, -100.0, 100.0);
        double scaleFactor = 1 / 16.0;
        GL11.glScaled(scaleFactor, scaleFactor, scaleFactor);
        // We need to end with the model-view matrix selected. It's what the rendering code expects.
        GL11.glMatrixMode(GL11.GL_MODELVIEW);

        RenderHelper.enableGUIStandardItemLighting();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);

    }

    private void teardownRenderState() {
        RenderTarget.forEach(RenderTarget::unbind);
    }
}
