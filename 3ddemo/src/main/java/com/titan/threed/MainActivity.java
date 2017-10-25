package com.titan.threed;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.esri.arcgisruntime.layers.ArcGISSceneLayer;
import com.esri.arcgisruntime.mapping.ArcGISScene;
import com.esri.arcgisruntime.mapping.ArcGISTiledElevationSource;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.Camera;
import com.esri.arcgisruntime.mapping.view.SceneView;

public class MainActivity extends AppCompatActivity {
    private SceneView mSceneView;

    private final  String SCENE_LAYER_URL="https://sampleserver6.arcgisonline.com/arcgis/rest/services/Elevation/WorldElevations/MapServer";
    private final  String SCENE_LAYER_URL1="http://scene.arcgis.com/arcgis/rest/services/Hosted/Buildings_San_Francisco/SceneServer/layers/0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // create a scene and add a basemap to it
        ArcGISScene scene = new ArcGISScene();
        scene.setBasemap(Basemap.createImagery());

        // add a scene layer
        ArcGISSceneLayer sceneLayer = new ArcGISSceneLayer(SCENE_LAYER_URL1);
        scene.getOperationalLayers().add(sceneLayer);

        // create SceneView from layout
        mSceneView = (SceneView) findViewById(R.id.sceneView);
        mSceneView.setScene(scene);
        // create an elevation source, and add this to the base surface of the scene
        ArcGISTiledElevationSource elevationSource = new ArcGISTiledElevationSource(
                "http://elevation3d.arcgis.com/arcgis/rest/services/WorldElevation3D/Terrain3D/ImageServer");
        scene.getBaseSurface().getElevationSources().add(elevationSource);

        // add a camera and initial camera position (Snowdonia National Park)
        Camera camera = new Camera(53.06, -4.04, 1289.0, 295.0, 71, 0.0);
        mSceneView.setViewpointCamera(camera);
    }
}
