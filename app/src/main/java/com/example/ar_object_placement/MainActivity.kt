package com.example.ar_object_placement

import android.content.Context
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.net.Uri
import android.opengl.GLSurfaceView
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.isGone
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.ar.core.Config
import com.google.ar.sceneform.math.Vector3
import com.xperiencelabs.arapp.CustomGLRenderer
import io.github.sceneview.ar.ArSceneView
import io.github.sceneview.ar.localScale
import io.github.sceneview.ar.node.ArModelNode
import io.github.sceneview.ar.node.AugmentedImageNode
import io.github.sceneview.ar.node.PlacementMode
import io.github.sceneview.material.setExternalTexture
import io.github.sceneview.math.Position
import io.github.sceneview.math.Rotation
import io.github.sceneview.math.Scale
import io.github.sceneview.node.VideoNode
import java.util.Vector

class MainActivity : AppCompatActivity(),View.OnTouchListener {

    private lateinit var sceneView: ArSceneView
    lateinit var placeButton: ExtendedFloatingActionButton
    private lateinit var modelNode: ArModelNode
   // private lateinit var videoNode: VideoNode
    private lateinit var mediaPlayer:MediaPlayer
    val scaleGestureDetector: ScaleGestureDetector by lazy {
        ScaleGestureDetector(this, ScaleListener())
    }
    private var currentScaleFactor = 1.0f

    private lateinit var glSurfaceView: GLSurfaceView
    private lateinit var customGLRenderer: CustomGLRenderer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //glSurfaceView.setEGLContextClientVersion(2)
       // glSurfaceView.setRenderer(myGLRenderer)


        sceneView = findViewById<ArSceneView?>(R.id.sceneView).apply {
            this.lightEstimationMode = Config.LightEstimationMode.DISABLED
        }

        mediaPlayer = MediaPlayer.create(this,R.raw.ad)
        placeButton = findViewById(R.id.place)
        sceneView.setOnTouchListener(this)

        placeButton.setOnClickListener {
            if(show)
                detachAnchor()
            else
                placeModel()
        }

//        videoNode = VideoNode(sceneView.engine, scaleToUnits = 0.7f, centerOrigin = Position(y=-4f), glbFileLocation = "models/plane.glb", player = mediaPlayer, onLoaded = {_,_ ->
//            mediaPlayer.start()
//        })

        modelNode = ArModelNode(sceneView.engine,PlacementMode.INSTANT).apply {
            loadModelGlbAsync(
                glbFileLocation = "models/sofa.glb",
                scaleToUnits = .7f,
                centerOrigin = Position(-0.5f)
            )
            {
                sceneView.planeRenderer.isVisible = true
                val materialInstance = it.materialInstances[0]
            }
            onAnchorChanged = { //  placeButton.isGone = it != null
            }
        }

        sceneView.addChild(modelNode)
        //modelNode.addChild(videoNode)

    }
    override fun onTouch(view: View, event: MotionEvent): Boolean {
        scaleGestureDetector.onTouchEvent(event)
        return true
    }
    var show =false

   private fun placeModel(){
       modelNode.anchor()
       show = true
   //  sceneView.planeRenderer.isVisible = false
   }

    fun detachAnchor() {
        show = false
        modelNode.anchor?.detach()
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer.stop()
    }
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
    private var lastScaleFactor = 1.0f
    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {


        override fun onScale(detector: ScaleGestureDetector): Boolean {
            val scaleFactor = detector.scaleFactor
            if(scaleFactor>1)
            {
                //zoomed in
                Log.d("sdfsfwerwer","zoomin" + lastScaleFactor)

                lastScaleFactor = ((lastScaleFactor+(0.5)).toFloat())
                Log.d("sdfsfwerwer",lastScaleFactor.toString())
                modelNode.scaleModel(lastScaleFactor)
            }
            else
            {


                lastScaleFactor = ((lastScaleFactor-(0.5)).toFloat())
                Log.d("sdfsfwerwer","zoomout" +lastScaleFactor )
                Log.d("sdfsfwerwer",lastScaleFactor.toString())
                modelNode.scaleModel(lastScaleFactor)
            }

            //Toast.makeText(applicationContext,"$deltaScale",Toast.LENGTH_LONG).show()

            // Adjust the scale of the modelNode based on the pinch gesture
          //  modelNode.localScale = Vector3(modelNode.localScale.x * deltaScale, modelNode.localScale.y * deltaScale, modelNode.localScale.z * deltaScale)

            // Save the last scale factor for the next pinch gesture
            lastScaleFactor = scaleFactor
            return true
        }

        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            lastScaleFactor = 1.0f
            return true
        }
    }

}
