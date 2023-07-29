package com.github.dcysteine.nesql.exporter.util.render;

import bq_standard.tasks.TaskHunt;
import codechicken.lib.gui.GuiDraw;
import codechicken.nei.guihook.GuiContainerManager;
import com.github.dcysteine.nesql.exporter.main.Logger;
import com.github.dcysteine.nesql.exporter.main.config.ConfigOptions;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
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
import java.util.Optional;
import static java.lang.Math.*;

/** Singleton class that handles rendering items and fluids and saving the resulting image data. */
public enum Renderer {
    INSTANCE;

    public static final String IMAGE_FILE_EXTENSION = ".png";
    private static final String IMAGE_FORMAT = "PNG";

    private int imageDim;
    private File imageDirectory;
    private Framebuffer framebuffer;

    // Used for intermittent logging.
    private int loggingCounter;

    /**
     * This method is meant to be called from the client thread, prior to setting the dispatcher
     * state to {@code INITIALIZING}. It performs initialization of non-render-related variables.
     */
    public void preinitialize(File imageDirectory) {
        this.imageDim = ConfigOptions.ICON_DIMENSION.get();
        this.imageDirectory = imageDirectory;
        this.loggingCounter = 0;
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
            for (int i = 0; i < ConfigOptions.RENDER_ICONS_PER_TICK.get(); i++) {
                Optional<RenderJob> jobOptional = RenderDispatcher.INSTANCE.getJob();
                if (!jobOptional.isPresent()) {
                    break;
                }

                RenderJob job = jobOptional.get();
                clearBuffer();
                render(job);
                BufferedImage image = readImage(job);

                File outputFile = new File(imageDirectory, job.getImageFilePath());
                // Not sure why, but this check fails spuriously every now and then.
                // It complains that the file exists, but I checked and it didn't actually exist.
                // Let's just... ignore it for now XD
                // The failures might be due to Windows getting confused by '~' in filenames XS
                /*
                if (outputFile.exists()) {
                    // If we cannot avoid queueing up duplicate render jobs, we can replace this
                    // throw with a continue, and move this check to before we call readImage(job)
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

    private void render(RenderJob job) {
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

            case ENTITY:
                GL11.glColor4f(1F, 1F, 1F, 1F);

                TaskHunt taskHunt = job.getEntity();
                Entity mob = null;
                if (EntityList.stringToClassMapping.containsKey(taskHunt.idName)) {
                    mob = EntityList.createEntityByName(taskHunt.idName, Minecraft.getMinecraft().theWorld);
                    if (mob != null) mob.readFromNBT(taskHunt.targetTags);
                }
                String mobName = EntityList.getEntityString(mob);
                int rectSize = 15;      // why is it limited to 16 for a model to be centered?
                int center = 8;
                float width, height, scale, posX, posY, rotation, pitch;

                switch (mobName) {
                    case "Automagy.WispNether":
                    case "Thaumcraft.Wisp":
                        rotation= 0F;
                        pitch = 90F;
                        scale = rectSize / mob.width;
                        posX = posY = center;
                        break;
                    case "BiomesOPlenty.Wasp":
                        rotation = -30F;
                        pitch = 15F;
                        width = (float) (abs(sin(rotation)) + abs(cos(rotation))) * mob.width;
                        scale = rectSize / width;
                        posX = center + 3;
                        posY = rectSize;
                        break;
                    case "DraconicEvolution.ChaosGuardian":
                        rotation = 150F;
                        pitch = 15F;
                        width = (float) (abs(sin(rotation)) + abs(cos(rotation))) * mob.width;
                        scale = rectSize * 2 / width;
                        posX = center;
                        posY = center + 3;
                        break;
                    case "Squid":
                    case "Ghast":
                    case "GalacticraftCore.EvolvedBossGhast":
                    case "HardcoreEnderExpansion.HauntedMiner":
                    case "HardcoreEnderExpansion.EnderEye":
                    case "HardcoreEnderExpansion.FireFiend":
                    case "HardcoreEnderExpansion.Louse":
                        rotation = -30F;
                        pitch = 15F;
                        width = (float) (abs(sin(rotation)) + abs(cos(rotation))) * mob.width;
                        height = (float) (abs(sin(pitch)) * mob.width + mob.height);
                        scale = width > height ? rectSize / width : rectSize / height;
                        posX = center;
                        posY = center + 3;
                        break;
                    case "Chicken":
                    case "Silverfish":
                    case "witchery.babayaga":
                    case "witchery.lordoftorment":
                        rotation = -30F;
                        pitch = 15F;
                        width = (float) (abs(sin(rotation)) + abs(cos(rotation))) * mob.width;
                        height = (float) (abs(sin(pitch)) * mob.width + mob.height);
                        scale = width > height ? (rectSize - 2) / width : (rectSize - 2) / height;
                        posX = center;
                        posY = rectSize;
                        break;
                    default:
                        rotation = -30F;
                        pitch = 15F;
                        width = (float) (abs(sin(rotation)) + abs(cos(rotation))) * mob.width;
                        height = (float) (abs(sin(pitch)) * mob.width + mob.height);
                        scale = width > height ? rectSize / width : rectSize / height;
                        posX = center;
                        posY = rectSize;
                }

                renderEntity(posX, posY, 0F, scale, rotation, pitch, mob);
                break;

            default:
                throw new IllegalArgumentException("Unrecognized job type: " + job);
        }
    }
    public void renderEntity(float posX, float posY, float posZ, float scale, float rotation, float pitch, Entity entity)  {
        try {
            GL11.glEnable(GL11.GL_COLOR_MATERIAL);
            GL11.glPushMatrix();
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glTranslatef(posX, posY, posZ);
            GL11.glScalef(-scale, scale, scale); // Not entirely sure why mobs are flipped but this is how vanilla GUIs fix it so...
            GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(pitch, 1F, 0F, 0F);
            GL11.glRotatef(rotation, 0F, 1F, 0F);
            float f3 = entity.rotationYaw;
            float f4 = entity.rotationPitch;
            RenderHelper.enableStandardItemLighting();
            RenderManager.instance.playerViewY = 180.0F;
            RenderManager.instance.renderEntityWithPosYaw(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
            entity.rotationYaw = f3;
            entity.rotationPitch = f4;
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glPopMatrix();
            RenderHelper.disableStandardItemLighting();
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
            GL11.glEnable(GL11.GL_TEXTURE_2D); // Breaks subsequent text rendering if not included
        } catch(Exception e) {
            // Hides rendering errors with entities which are common for invalid/technical entities
        }
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

    private void clearBuffer() {
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

        framebuffer.bindFramebuffer(true);
        // Do we need to bind GL_DRAW_FRAMEBUFFER here as well? Seems to work fine as-is though...
        OpenGlHelper.func_153171_g(GL30.GL_READ_FRAMEBUFFER, framebuffer.framebufferObject);
    }

    private void teardownRenderState() {
        framebuffer.unbindFramebuffer();
    }
}
