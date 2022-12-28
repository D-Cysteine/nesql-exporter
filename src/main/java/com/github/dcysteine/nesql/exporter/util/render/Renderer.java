package com.github.dcysteine.nesql.exporter.util.render;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.guihook.GuiContainerManager;
import com.github.dcysteine.nesql.exporter.main.Logger;
import com.github.dcysteine.nesql.exporter.main.config.ConfigOptions;
import com.github.dcysteine.nesql.sql.util.IdUtil;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.Fluid;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL30;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Optional;

/** Singleton class that handles rendering items and fluids and saving the resulting image data. */
public enum Renderer {
    INSTANCE;

    private static final String ITEM_DIRECTORY_PATH =
            "nesql" + File.separator + "item";
    private static final String FLUID_DIRECTORY_PATH =
            "nesql" + File.separator + "fluid";

    private int imageDim;
    private Framebuffer framebuffer;
    private File itemDirectory;
    private File fluidDirectory;

    /**
     * This method is meant to be called from the export thread, prior to setting the dispatcher
     * state to {@code INITIALIZING}. It performs initialization of non-render-related variables.
     */
    public void preinitialize(File itemDirectory, File fluidDirectory) {
        this.imageDim = ConfigOptions.ICON_DIMENSION.get();
        this.itemDirectory = itemDirectory;
        this.fluidDirectory = fluidDirectory;

        if ((itemDirectory.exists() || !itemDirectory.mkdirs())
                || (fluidDirectory.exists() || !fluidDirectory.mkdirs())) {
            Logger.chatMessage(
                    EnumChatFormatting.RED + "Could not create images directories!");
            Logger.chatMessage(EnumChatFormatting.RED + "Skipping rendering!");
            RenderDispatcher.INSTANCE.setRendererState(RenderDispatcher.RendererState.ERROR);
        }
    }

    /**
     * Perform initialization of render-related variables, which must be done on the render thread.
     */
    private void initialize() {
        this.framebuffer = new Framebuffer(imageDim, imageDim, true);

        RenderDispatcher.INSTANCE.setRendererState(RenderDispatcher.RendererState.INITIALIZED);
    }

    /**
     * Perform destruction of render-related variables, which must be done on the render thread.
     */
    private void destroy() {
        framebuffer.deleteFramebuffer();
        framebuffer = null;

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

            case DESTROYING:
                destroy();
                return;
        }

        if (RenderDispatcher.INSTANCE.noJobsRemaining()) {
            RenderDispatcher.INSTANCE.notifyJobsCompleted();
            return;
        }

        setupRenderState();
        for (int i = 0; i < ConfigOptions.RENDER_ICONS_PER_TICK.get(); i++) {
            Optional<RenderJob> jobOptional = RenderDispatcher.INSTANCE.getJob();
            if (!jobOptional.isPresent()) {
                break;
            }

            RenderJob job = jobOptional.get();
            clearBuffer();
            render(job);
            BufferedImage image = readImage(job);

            File outputFile;
            // TODO the names here need to be set to the unique SQL row key.
            switch (job.type()) {
                case ITEM:
                    outputFile = new File(itemDirectory, IdUtil.itemId(job.item().stack()) + ".png");
                    break;

                case FLUID:
                    outputFile = new File(fluidDirectory, IdUtil.fluidId(job.fluid()) + ".png");
                    break;

                default:
                    throw new IllegalArgumentException("Unrecognized job type: " + job);
            }
            if (outputFile.exists()) {
                // If we cannot avoid queueing up duplicate render jobs, we can replace this throw
                // with a continue, and move this check to before we call readImage(job)
                throw new RuntimeException(
                        "Render output file already exists: " + outputFile.getPath());
            }

            try {
                ImageIO.write(image, "PNG", outputFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        teardownRenderState();
    }

    private void clearBuffer() {
        // Parameters are RGBA. Set full transparent background.
        GL11.glClearColor(0f, 0f, 0f, 0f);
        GL11.glClearDepth(1D);
        GL11.glClear(16384 | 256);
    }

    private void render(RenderJob job) {
        switch (job.type()) {
            case ITEM:
                GuiContainerManager.drawItem(0, 0, job.item().stack());
                break;

            case FLUID:
                Fluid fluid = job.fluid();
                IIcon icon = fluid.getIcon();
                // Some fluids don't set their icon colour, so we have to blend in the colour.
                int colour = fluid.getColor();
                GL11.glColor3ub(
                        (byte) ((colour & 0xFF0000) >> 16),
                        (byte) ((colour & 0x00FF00) >> 8),
                        (byte) (colour & 0x0000FF));

                GuiDraw.changeTexture(TextureMap.locationBlocksTexture);
                GuiDraw.gui.drawTexturedModelRectFromIcon(0, 0, icon, 16, 16);

                // Reset colour blending.
                GL11.glColor4f(1f, 1f, 1f, 1f);
                break;

            default:
                throw new IllegalArgumentException("Unrecognized job type: " + job);
        }
    }

    private void setupRenderState() {
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPushMatrix();
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPushMatrix();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0, 1.0, 1.0, 0.0, -100.0, 100.0);
        GL11.glPushMatrix();
        double scaleFactor = 1 / 16.0;
        GL11.glScaled(scaleFactor, scaleFactor, scaleFactor);
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glFrontFace(GL11.GL_CCW);
        GL11.glEnable(GL11.GL_DEPTH_TEST);

        setupLighting();

        framebuffer.bindFramebuffer(true);
        // Do we need to bind GL_DRAW_FRAMEBUFFER here as well? Seems to work fine as-is though...
        OpenGlHelper.func_153171_g(GL30.GL_READ_FRAMEBUFFER, framebuffer.framebufferObject);
    }

    private void setupLighting() {
        // We have to reset these lighting variables each time, since Minecraft seems to reset them.
        // GL11.GL_LIGHT0 looks like it's used already, so use GL11.GL_LIGHT1 just to reduce chances
        // of breaking stuff.
        FloatBuffer ambientColour = BufferUtils.createFloatBuffer(4);
        ambientColour.put(new float[]{0.1f, 0.1f, 0.1f, 1f}).flip();
        GL11.glLight(GL11.GL_LIGHT1, GL11.GL_AMBIENT, ambientColour);

        FloatBuffer diffuseColour = BufferUtils.createFloatBuffer(4);
        diffuseColour.put(new float[]{1f, 1f, 1f, 1f}).flip();
        GL11.glLight(GL11.GL_LIGHT1, GL11.GL_DIFFUSE, diffuseColour);

        IntBuffer lightPosition = BufferUtils.createIntBuffer(4);
        lightPosition.put(new int[]{-1, 6, -1, 0}).flip();
        GL11.glLight(GL11.GL_LIGHT1, GL11.GL_POSITION, lightPosition);

        // Not sure why, but we get better results with GL_LIGHTING disabled.
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_LIGHT0);
        GL11.glEnable(GL11.GL_LIGHT1);
        GL11.glShadeModel(GL11.GL_FLAT);
    }

    private void teardownRenderState() {
        framebuffer.unbindFramebuffer();

        GL11.glPopAttrib();
        GL11.glPopMatrix();
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPopMatrix();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPopMatrix();
    }

    /** Returns the rendered image, in {@link BufferedImage#TYPE_INT_ARGB} format. */
    private BufferedImage readImage(RenderJob job) {
        ByteBuffer imageByteBuffer = BufferUtils.createByteBuffer(4 * imageDim * imageDim);
        GL11.glReadPixels(
                0, 0, imageDim, imageDim,
                GL12.GL_BGRA, GL11.GL_UNSIGNED_BYTE, imageByteBuffer);

        // OpenGL uses inverted y-coordinates compared to our draw methods.
        // So we must flip the saved image vertically.
        //
        // Unfortunately, for some reason, the rendering seems to break if we try to invert using
        // OpenGL matrix transforms, so let's just do this on the pixel array.
        int[] pixels = new int[imageDim * imageDim];
        imageByteBuffer.asIntBuffer().get(pixels);
        int[] flippedPixels = new int[pixels.length];
        for (int i = 0; i < pixels.length; i++) {
            int x = i % imageDim;
            int y = imageDim - (i / imageDim + 1);
            flippedPixels[i] = pixels[x + imageDim * y];
        }

        BufferedImage image =
                new BufferedImage(imageDim, imageDim, BufferedImage.TYPE_INT_ARGB);
        image.setRGB(0, 0, imageDim, imageDim, flippedPixels, 0, imageDim);
        return image;
    }
}
