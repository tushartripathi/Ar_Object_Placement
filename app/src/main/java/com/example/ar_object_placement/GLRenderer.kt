package com.xperiencelabs.arapp

import android.opengl.EGLConfig
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import javax.microedition.khronos.opengles.GL10


class CustomGLRenderer : GLSurfaceView.Renderer {
    // Add any variables or helper functions related to the shaders here.

    override fun onSurfaceCreated(gl: GL10?, config: javax.microedition.khronos.egl.EGLConfig?) {

    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        // Adjust OpenGL viewport and other settings when the surface changes size.
        GLES20.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        // Apply the GLSL shaders to modify the appearance of the 3D model here.
        // You can change color, brightness, or add animation effects.

        // Clear the screen.
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // Add your GLSL shader code here to modify the appearance of the 3D model.
        // For example:
        // GLES20.glUseProgram(yourShaderProgram)
        // ... (Set shader uniforms and attributes)
        // modelNode.draw()
    }
}
