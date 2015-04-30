package mygame;

import com.jme3.animation.Bone;
import com.jme3.animation.Skeleton;
import com.jme3.animation.SkeletonControl;
import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioNode;
import com.jme3.audio.LowPassFilter;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.post.filters.DepthOfFieldFilter;
import com.jme3.post.filters.LightScatteringFilter;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import com.jme3.ui.Picture;
import com.jme3.util.BufferUtils;
import com.jme3.util.SkyFactory;
import com.jme3.water.WaterFilter;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.prefs.BackingStoreException;

/*
 * Max Huddleston
 * CSC 582 Assignment 4
 * 5 April 2015
 */

public class Main extends SimpleApplication {
    Scanner input = new Scanner(System.in);
    
    Node settlements = new Node("setts"); //add all settlements to this node
    Node board = new Node("gameBoard"); //add all hexes, settlements, and roads to this node
    
    float floorRotate = FastMath.DEG_TO_RAD *-60f; //gameboard rotate
    float distance = 0;
    
    /*see end of makeHexes for graph layout*/
    float[] xArray = {0,1.732f,-1.732f,-2.598f,-0.866f,0.866f,2.598f,
                        3.464f,1.732f,0,-1.732f,-3.464f,-2.595f,
                        -0.866f,0.866f,2.595f,1.732f,0,-1.732f};

    float[] yArray = {0,0,0,1.5f,1.5f,1.5f,1.5f,3,3,3,3,3,
                        4.5f,4.5f,4.5f,4.5f,6,6,6};
    
    //represents hexes 1 - 19
    int[] indexArray = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18};
    //dice values assigned to hexes
    int[] hexValue =   {2,2,3,3,4,4,5,5,6,6, 8, 8, 9, 9,10,10,11,11,12}; //extra 2 instead of 7 (7 = robber)
    //rolled dice values assigned to 
    int[] resourceHex = {0,0,0}; //can hold 1 or 2 resources
    
    String[] resourcesP1 = {"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""}; //initialize //hold up to 14 cards
    int rsrcCounterP1 = 0;
    
    //TRIGGERS
    private final static Trigger TRIGGER_COLOR1 = new KeyTrigger(KeyInput.KEY_X);
    private final static Trigger TRIGGER_COLOR2 = new KeyTrigger(KeyInput.KEY_C);
    private final static Trigger TRIGGER_COLOR3 = new KeyTrigger(KeyInput.KEY_V);
    private final static Trigger TRIGGER_PLACEMENT = new MouseButtonTrigger(MouseInput.BUTTON_LEFT);
    private final static Trigger ROLL = new KeyTrigger(KeyInput.KEY_R); //roll dice

    
    //MAPPINGS
    private final static String MAPPING_COLOR1 = "Toggle Color1";
    private final static String MAPPING_COLOR2 = "Toggle Color2";
    private final static String MAPPING_COLOR3 = "Toggle Color3";
    private final static String MAPPING_PLACEMENT = "Settlement Placement";
    private final static String MAPPING_ROLL = "Toggle";
    
    Vector3f contactpt; //inside analog listener
    
    //declare objects that require action done on them
    public static Geometry redRoad1;
    public static Geometry redRoad2;
    public static Geometry redRoad3;
    public Geometry dice1;
    public Geometry dice2;
    
    private Bone bone;
    private Skeleton skeleton;
    private Quaternion rotation = new Quaternion();
    //GUI NODE
    private BitmapText diceText;
    private BitmapText resourceHexText;
    private BitmapText resourceListText;
    private BitmapText WinText;
    public String resource1text = "";
    public String resource2text = "";
    public int redDie, ylwDie;
    //TRIANGLE
    private static Box sampleMesh = new Box(Vector3f.ZERO, 1, 1, 1);
    
    Spatial settlement1_1;
    Spatial settlement1_2;
    Spatial settlement1_3;
    Spatial settlement1_4;
    Spatial settlement1_5;
    
    float newYLocation;
    float newXLocation;
    boolean placement = false;
    
    //AUDIO
    private Vector3f lightDir = new Vector3f(-4.9236743f, -1.27054665f, 5.896916f);
    private WaterFilter water;
    TerrainQuad terrain;
    Material matRock;
    AudioNode waves;
    AudioNode drum;
    AudioNode mainTheme;
    LowPassFilter underWaterAudioFilter = new LowPassFilter(0.5f, 0.1f);
    LowPassFilter underWaterReverbFilter = new LowPassFilter(0.5f, 0.1f);
    LowPassFilter aboveWaterAudioFilter = new LowPassFilter(1, 1);
    
    //shufle class
    int diceSum = 2; //initialize
    int[] diceArr = {1,2,3,4,5,6};
    Shuffle shuffle = new Shuffle(diceArr);
    
    
    //starts JMonkey app
    public static void main(String[] args) throws BackingStoreException {
        AppSettings settings = new AppSettings(true);
        settings.setTitle("Catan 3D");
        settings.setSettingsDialogImage("Interface/catanSplash.jpg");
        settings.setResolution(800, 600);   

        Main app = new Main();

        app.setSettings(settings);
        app.start();
    }

    //initialize scene here
    @Override
    public void simpleInitApp() {
        flyCam.setEnabled(false);
        
        makeHexes();
        makeRoads();
        makeModels();
        makeDice();
        makeGUInode();
        createSky();
        //rollDice();
        //createWater();
        
        mainTheme = new AudioNode(assetManager, "Sounds/Video_Game_Atmosphere_Main.ogg");
        //mainTheme.play();
        //want each button to only change one settlement color at a time
        //but each button changes all three settlement colors
        //map one color for each trigger
        inputManager.addMapping(MAPPING_COLOR1, TRIGGER_COLOR1);
        inputManager.addMapping(MAPPING_COLOR2, TRIGGER_COLOR2);
        inputManager.addMapping(MAPPING_COLOR3, TRIGGER_COLOR3);
        inputManager.addMapping(MAPPING_PLACEMENT, TRIGGER_PLACEMENT);
        inputManager.addMapping(MAPPING_ROLL, ROLL);
        
        //map rotation function for future use
        inputManager.addListener(actionListener, new String[]{MAPPING_COLOR1, MAPPING_COLOR2, MAPPING_COLOR3});
        inputManager.addListener(actionListener, new String[]{MAPPING_PLACEMENT});
        inputManager.addListener(actionListener, new String[]{MAPPING_ROLL});
        
        drum = new AudioNode(assetManager, "Sounds/drum.wav");
        drum.setVolume(10);
        //waves.setVolume(1);
    }

    
    public void makeHexes() {
        //19 hexes
        Mesh mesh = new Mesh();
        
        Vector3f [] vertices = new Vector3f[3];
        vertices[0] = new Vector3f(0,0,0);
        vertices[1] = new Vector3f(0.866f,0.5f,0);
        vertices[2] = new Vector3f(0,1,0);
        
        Vector2f[] texCoord = new Vector2f[3];
        texCoord[0] = new Vector2f(0,0);
        texCoord[1] = new Vector2f(1,0);
        texCoord[2] = new Vector2f(0,1);
        
        int [] indexes = {2,0,1};
        
        mesh.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        mesh.setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(texCoord));
        mesh.setBuffer(Type.Index,    3, BufferUtils.createIntBuffer(indexes));
        mesh.updateBound();
        
        // using 6 mesh triangles to make 1 hex
        //need 19 hexes
        //hex 1
        Geometry tri11 = new Geometry("geom1-1", mesh); 
        Geometry tri12 = new Geometry("geom1-2", mesh);
        Geometry tri13 = new Geometry("geom1-3", mesh);
        Geometry tri14 = new Geometry("geom1-4", mesh);
        Geometry tri15 = new Geometry("geom1-5", mesh);
        Geometry tri16 = new Geometry("geom1-6", mesh);
        //hex 2
        Geometry tri21 = new Geometry("geom2-1", mesh); 
        Geometry tri22 = new Geometry("geom2-2", mesh);
        Geometry tri23 = new Geometry("geom2-3", mesh);
        Geometry tri24 = new Geometry("geom2-4", mesh);
        Geometry tri25 = new Geometry("geom2-5", mesh);
        Geometry tri26 = new Geometry("geom2-6", mesh);
        //hex 3
        Geometry tri31 = new Geometry("geom3-1", mesh); 
        Geometry tri32 = new Geometry("geom3-2", mesh);
        Geometry tri33 = new Geometry("geom3-3", mesh);
        Geometry tri34 = new Geometry("geom3-4", mesh);
        Geometry tri35 = new Geometry("geom3-5", mesh);
        Geometry tri36 = new Geometry("geom3-6", mesh);
        //hex 4
        Geometry tri41 = new Geometry("geom4-1", mesh); 
        Geometry tri42 = new Geometry("geom4-2", mesh);
        Geometry tri43 = new Geometry("geom4-3", mesh);
        Geometry tri44 = new Geometry("geom4-4", mesh);
        Geometry tri45 = new Geometry("geom4-5", mesh);
        Geometry tri46 = new Geometry("geom4-6", mesh);
        //hex 5
        Geometry tri51 = new Geometry("geom5-1", mesh); 
        Geometry tri52 = new Geometry("geom5-2", mesh);
        Geometry tri53 = new Geometry("geom5-3", mesh);
        Geometry tri54 = new Geometry("geom5-4", mesh);
        Geometry tri55 = new Geometry("geom5-5", mesh);
        Geometry tri56 = new Geometry("geom5-6", mesh);
        //hex 6
        Geometry tri61 = new Geometry("geom6-1", mesh); 
        Geometry tri62 = new Geometry("geom6-2", mesh);
        Geometry tri63 = new Geometry("geom6-3", mesh);
        Geometry tri64 = new Geometry("geom6-4", mesh);
        Geometry tri65 = new Geometry("geom6-5", mesh);
        Geometry tri66 = new Geometry("geom6-6", mesh);
        //hex 7
        Geometry tri71 = new Geometry("geom7-1", mesh); 
        Geometry tri72 = new Geometry("geom7-2", mesh);
        Geometry tri73 = new Geometry("geom7-3", mesh);
        Geometry tri74 = new Geometry("geom7-4", mesh);
        Geometry tri75 = new Geometry("geom7-5", mesh);
        Geometry tri76 = new Geometry("geom7-6", mesh);
        //hex 8
        Geometry tri81 = new Geometry("geom8-1", mesh); 
        Geometry tri82 = new Geometry("geom8-2", mesh);
        Geometry tri83 = new Geometry("geom8-3", mesh);
        Geometry tri84 = new Geometry("geom8-4", mesh);
        Geometry tri85 = new Geometry("geom8-5", mesh);
        Geometry tri86 = new Geometry("geom8-6", mesh);
        //hex 9
        Geometry tri91 = new Geometry("geom9-1", mesh); 
        Geometry tri92 = new Geometry("geom9-2", mesh);
        Geometry tri93 = new Geometry("geom9-3", mesh);
        Geometry tri94 = new Geometry("geom9-4", mesh);
        Geometry tri95 = new Geometry("geom9-5", mesh);
        Geometry tri96 = new Geometry("geom9-6", mesh);
        //hex 10
        Geometry tri101 = new Geometry("geom10-1", mesh); 
        Geometry tri102 = new Geometry("geom10-2", mesh);
        Geometry tri103 = new Geometry("geom10-3", mesh);
        Geometry tri104 = new Geometry("geom10-4", mesh);
        Geometry tri105 = new Geometry("geom10-5", mesh);
        Geometry tri106 = new Geometry("geom10-6", mesh);
        //hex 11
        Geometry tri111 = new Geometry("geom11-1", mesh); 
        Geometry tri112 = new Geometry("geom11-2", mesh);
        Geometry tri113 = new Geometry("geom11-3", mesh);
        Geometry tri114 = new Geometry("geom11-4", mesh);
        Geometry tri115 = new Geometry("geom11-5", mesh);
        Geometry tri116 = new Geometry("geom11-6", mesh);
        //hex 12
        Geometry tri121 = new Geometry("geom12-1", mesh); 
        Geometry tri122 = new Geometry("geom12-2", mesh);
        Geometry tri123 = new Geometry("geom12-3", mesh);
        Geometry tri124 = new Geometry("geom12-4", mesh);
        Geometry tri125 = new Geometry("geom12-5", mesh);
        Geometry tri126 = new Geometry("geom12-6", mesh);
        //hex 13
        Geometry tri131 = new Geometry("geom13-1", mesh); 
        Geometry tri132 = new Geometry("geom13-2", mesh);
        Geometry tri133 = new Geometry("geom13-3", mesh);
        Geometry tri134 = new Geometry("geom13-4", mesh);
        Geometry tri135 = new Geometry("geom13-5", mesh);
        Geometry tri136 = new Geometry("geom13-6", mesh);
        //hex 14
        Geometry tri141 = new Geometry("geom14-1", mesh); 
        Geometry tri142 = new Geometry("geom14-2", mesh);
        Geometry tri143 = new Geometry("geom14-3", mesh);
        Geometry tri144 = new Geometry("geom14-4", mesh);
        Geometry tri145 = new Geometry("geom14-5", mesh);
        Geometry tri146 = new Geometry("geom14-6", mesh);
        //hex 15
        Geometry tri151 = new Geometry("geom15-1", mesh); 
        Geometry tri152 = new Geometry("geom15-2", mesh);
        Geometry tri153 = new Geometry("geom15-3", mesh);
        Geometry tri154 = new Geometry("geom15-4", mesh);
        Geometry tri155 = new Geometry("geom15-5", mesh);
        Geometry tri156 = new Geometry("geom15-6", mesh);
        //hex 16
        Geometry tri161 = new Geometry("geom16-1", mesh); 
        Geometry tri162 = new Geometry("geom16-2", mesh);
        Geometry tri163 = new Geometry("geom16-3", mesh);
        Geometry tri164 = new Geometry("geom16-4", mesh);
        Geometry tri165 = new Geometry("geom16-5", mesh);
        Geometry tri166 = new Geometry("geom16-6", mesh);
        //hex 17
        Geometry tri171 = new Geometry("geom17-1", mesh); 
        Geometry tri172 = new Geometry("geom17-2", mesh);
        Geometry tri173 = new Geometry("geom17-3", mesh);
        Geometry tri174 = new Geometry("geom17-4", mesh);
        Geometry tri175 = new Geometry("geom17-5", mesh);
        Geometry tri176 = new Geometry("geom17-6", mesh);
        //hex 18
        Geometry tri181 = new Geometry("geom18-1", mesh); 
        Geometry tri182 = new Geometry("geom18-2", mesh);
        Geometry tri183 = new Geometry("geom18-3", mesh);
        Geometry tri184 = new Geometry("geom18-4", mesh);
        Geometry tri185 = new Geometry("geom18-5", mesh);
        Geometry tri186 = new Geometry("geom18-6", mesh);
        //hex 19
        Geometry tri191 = new Geometry("geom19-1", mesh); 
        Geometry tri192 = new Geometry("geom19-2", mesh);
        Geometry tri193 = new Geometry("geom19-3", mesh);
        Geometry tri194 = new Geometry("geom19-4", mesh);
        Geometry tri195 = new Geometry("geom19-5", mesh);
        Geometry tri196 = new Geometry("geom19-6", mesh);

        
        //triangle materials
        Material mat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md"); //blue
        Material mat2 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md"); //yellow
        Material mat3 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md"); //green
        Material mat4 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md"); //red
        Material mat5 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md"); //gray
        Material mat6 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md"); //white
        
        mat1.setColor("Color", ColorRGBA.Blue);  //sheep
        mat2.setColor("Color", ColorRGBA.Yellow);//wheat
        mat3.setColor("Color", ColorRGBA.Green); //lumber
        mat4.setColor("Color", ColorRGBA.Red);   //brick
        mat5.setColor("Color", ColorRGBA.Gray);  //ore
        mat6.setColor("Color", ColorRGBA.White); //desert
        
        /* # of each hex type
         * 4 sheep
         * 4 wheat
         * 4 lumber
         * 3 brick
         * 3 ore
         * 1 desert*/
        
        //sheep 1
        tri11.setMaterial(mat1);
        tri12.setMaterial(mat1);
        tri13.setMaterial(mat1);
        tri14.setMaterial(mat1);
        tri15.setMaterial(mat1);
        tri16.setMaterial(mat1);
        //sheep 2
        tri21.setMaterial(mat1);
        tri22.setMaterial(mat1);
        tri23.setMaterial(mat1);
        tri24.setMaterial(mat1);
        tri25.setMaterial(mat1);
        tri26.setMaterial(mat1);
        //sheep 3
        tri31.setMaterial(mat1);
        tri32.setMaterial(mat1);
        tri33.setMaterial(mat1);
        tri34.setMaterial(mat1);
        tri35.setMaterial(mat1);
        tri36.setMaterial(mat1);
        //sheep 4
        tri41.setMaterial(mat1);
        tri42.setMaterial(mat1);
        tri43.setMaterial(mat1);
        tri44.setMaterial(mat1);
        tri45.setMaterial(mat1);
        tri46.setMaterial(mat1);
        //wheat 1
        tri51.setMaterial(mat2);
        tri52.setMaterial(mat2);
        tri53.setMaterial(mat2);
        tri54.setMaterial(mat2);
        tri55.setMaterial(mat2);
        tri56.setMaterial(mat2);
        //wheat 2
        tri61.setMaterial(mat2);
        tri62.setMaterial(mat2);
        tri63.setMaterial(mat2);
        tri64.setMaterial(mat2);
        tri65.setMaterial(mat2);
        tri66.setMaterial(mat2);
        //wheat 3
        tri71.setMaterial(mat2);
        tri72.setMaterial(mat2);
        tri73.setMaterial(mat2);
        tri74.setMaterial(mat2);
        tri75.setMaterial(mat2);
        tri76.setMaterial(mat2);
        //wheat 4
        tri81.setMaterial(mat2);
        tri82.setMaterial(mat2);
        tri83.setMaterial(mat2);
        tri84.setMaterial(mat2);
        tri85.setMaterial(mat2);
        tri86.setMaterial(mat2);
        //lumber 1
        tri91.setMaterial(mat3);
        tri92.setMaterial(mat3);
        tri93.setMaterial(mat3);
        tri94.setMaterial(mat3);
        tri95.setMaterial(mat3);
        tri96.setMaterial(mat3);
        //lumber 2
        tri101.setMaterial(mat3);
        tri102.setMaterial(mat3);
        tri103.setMaterial(mat3);
        tri104.setMaterial(mat3);
        tri105.setMaterial(mat3);
        tri106.setMaterial(mat3);
        //lumber 3
        tri111.setMaterial(mat3);
        tri112.setMaterial(mat3);
        tri113.setMaterial(mat3);
        tri114.setMaterial(mat3);
        tri115.setMaterial(mat3);
        tri116.setMaterial(mat3);
        //lumber 4
        tri121.setMaterial(mat3);
        tri122.setMaterial(mat3);
        tri123.setMaterial(mat3);
        tri124.setMaterial(mat3);
        tri125.setMaterial(mat3);
        tri126.setMaterial(mat3);
        //brick 1
        tri131.setMaterial(mat4);
        tri132.setMaterial(mat4);
        tri133.setMaterial(mat4);
        tri134.setMaterial(mat4);
        tri135.setMaterial(mat4);
        tri136.setMaterial(mat4);
        //brick 2
        tri141.setMaterial(mat4);
        tri142.setMaterial(mat4);
        tri143.setMaterial(mat4);
        tri144.setMaterial(mat4);
        tri145.setMaterial(mat4);
        tri146.setMaterial(mat4);
        //brick 3
        tri151.setMaterial(mat4);
        tri152.setMaterial(mat4);
        tri153.setMaterial(mat4);
        tri154.setMaterial(mat4);
        tri155.setMaterial(mat4);
        tri156.setMaterial(mat4);
        //ore 1
        tri161.setMaterial(mat5);
        tri162.setMaterial(mat5);
        tri163.setMaterial(mat5);
        tri164.setMaterial(mat5);
        tri165.setMaterial(mat5);
        tri166.setMaterial(mat5);
        //ore 2
        tri171.setMaterial(mat5);
        tri172.setMaterial(mat5);
        tri173.setMaterial(mat5);
        tri174.setMaterial(mat5);
        tri175.setMaterial(mat5);
        tri176.setMaterial(mat5);
        //ore 3
        tri181.setMaterial(mat5);
        tri182.setMaterial(mat5);
        tri183.setMaterial(mat5);
        tri184.setMaterial(mat5);
        tri185.setMaterial(mat5);
        tri186.setMaterial(mat5);
        //desert
        tri191.setMaterial(mat6);
        tri192.setMaterial(mat6);
        tri193.setMaterial(mat6);
        tri194.setMaterial(mat6);
        tri195.setMaterial(mat6);
        tri196.setMaterial(mat6);
        
        
        //rotate triangles to make hex shape
        float rotate1 = FastMath.DEG_TO_RAD *30f;
        float rotate2 = FastMath.DEG_TO_RAD *90f;
        float rotate3 = FastMath.DEG_TO_RAD *150f;
        float rotate4 = FastMath.DEG_TO_RAD *210f;
        float rotate5 = FastMath.DEG_TO_RAD *270f;
        float rotate6 = FastMath.DEG_TO_RAD *330f;

        float hexRotate = FastMath.DEG_TO_RAD *30f; //adjust hexes to fit together
        
        //create hexagons by rotating triangles
        //sheep 1
        tri11.rotate(0,0, rotate1);
        tri12.rotate(0,0, rotate2);
        tri13.rotate(0,0, rotate3);
        tri14.rotate(0,0, rotate4);
        tri15.rotate(0,0, rotate5);
        tri16.rotate(0,0, rotate6);
        //sheep 2
        tri21.rotate(0,0, rotate1);
        tri22.rotate(0,0, rotate2);
        tri23.rotate(0,0, rotate3);
        tri24.rotate(0,0, rotate4);
        tri25.rotate(0,0, rotate5);
        tri26.rotate(0,0, rotate6);
        //sheep 3
        tri31.rotate(0,0, rotate1);
        tri32.rotate(0,0, rotate2);
        tri33.rotate(0,0, rotate3);
        tri34.rotate(0,0, rotate4);
        tri35.rotate(0,0, rotate5);
        tri36.rotate(0,0, rotate6);
        //sheep 4
        tri41.rotate(0,0, rotate1);
        tri42.rotate(0,0, rotate2);
        tri43.rotate(0,0, rotate3);
        tri44.rotate(0,0, rotate4);
        tri45.rotate(0,0, rotate5);
        tri46.rotate(0,0, rotate6);
        //wheat 1
        tri51.rotate(0,0, rotate1);
        tri52.rotate(0,0, rotate2);
        tri53.rotate(0,0, rotate3);
        tri54.rotate(0,0, rotate4);
        tri55.rotate(0,0, rotate5);
        tri56.rotate(0,0, rotate6);
        //wheat 2
        tri61.rotate(0,0, rotate1);
        tri62.rotate(0,0, rotate2);
        tri63.rotate(0,0, rotate3);
        tri64.rotate(0,0, rotate4);
        tri65.rotate(0,0, rotate5);
        tri66.rotate(0,0, rotate6);
        //wheat 3
        tri71.rotate(0,0, rotate1);
        tri72.rotate(0,0, rotate2);
        tri73.rotate(0,0, rotate3);
        tri74.rotate(0,0, rotate4);
        tri75.rotate(0,0, rotate5);
        tri76.rotate(0,0, rotate6);
        //wheat 4
        tri81.rotate(0,0, rotate1);
        tri82.rotate(0,0, rotate2);
        tri83.rotate(0,0, rotate3);
        tri84.rotate(0,0, rotate4);
        tri85.rotate(0,0, rotate5);
        tri86.rotate(0,0, rotate6);
        //lumber 1
        tri91.rotate(0,0, rotate1);
        tri92.rotate(0,0, rotate2);
        tri93.rotate(0,0, rotate3);
        tri94.rotate(0,0, rotate4);
        tri95.rotate(0,0, rotate5);
        tri96.rotate(0,0, rotate6);
        //lumber 2
        tri101.rotate(0,0, rotate1);
        tri102.rotate(0,0, rotate2);
        tri103.rotate(0,0, rotate3);
        tri104.rotate(0,0, rotate4);
        tri105.rotate(0,0, rotate5);
        tri106.rotate(0,0, rotate6);
        //lumber 3
        tri111.rotate(0,0, rotate1);
        tri112.rotate(0,0, rotate2);
        tri113.rotate(0,0, rotate3);
        tri114.rotate(0,0, rotate4);
        tri115.rotate(0,0, rotate5);
        tri116.rotate(0,0, rotate6);
        //lumber 4
        tri121.rotate(0,0, rotate1);
        tri122.rotate(0,0, rotate2);
        tri123.rotate(0,0, rotate3);
        tri124.rotate(0,0, rotate4);
        tri125.rotate(0,0, rotate5);
        tri126.rotate(0,0, rotate6);
        //brick 1
        tri131.rotate(0,0, rotate1);
        tri132.rotate(0,0, rotate2);
        tri133.rotate(0,0, rotate3);
        tri134.rotate(0,0, rotate4);
        tri135.rotate(0,0, rotate5);
        tri136.rotate(0,0, rotate6);
        //brick 2
        tri141.rotate(0,0, rotate1);
        tri142.rotate(0,0, rotate2);
        tri143.rotate(0,0, rotate3);
        tri144.rotate(0,0, rotate4);
        tri145.rotate(0,0, rotate5);
        tri146.rotate(0,0, rotate6);
        //brick 3
        tri151.rotate(0,0, rotate1);
        tri152.rotate(0,0, rotate2);
        tri153.rotate(0,0, rotate3);
        tri154.rotate(0,0, rotate4);
        tri155.rotate(0,0, rotate5);
        tri156.rotate(0,0, rotate6);
        //ore 1
        tri161.rotate(0,0, rotate1);
        tri162.rotate(0,0, rotate2);
        tri163.rotate(0,0, rotate3);
        tri164.rotate(0,0, rotate4);
        tri165.rotate(0,0, rotate5);
        tri166.rotate(0,0, rotate6);
        //ore 2
        tri171.rotate(0,0, rotate1);
        tri172.rotate(0,0, rotate2);
        tri173.rotate(0,0, rotate3);
        tri174.rotate(0,0, rotate4);
        tri175.rotate(0,0, rotate5);
        tri176.rotate(0,0, rotate6);
        //ore 3
        tri181.rotate(0,0, rotate1);
        tri182.rotate(0,0, rotate2);
        tri183.rotate(0,0, rotate3);
        tri184.rotate(0,0, rotate4);
        tri185.rotate(0,0, rotate5);
        tri186.rotate(0,0, rotate6);
        //brick
        tri191.rotate(0,0, rotate1);
        tri192.rotate(0,0, rotate2);
        tri193.rotate(0,0, rotate3);
        tri194.rotate(0,0, rotate4);
        tri195.rotate(0,0, rotate5);
        tri196.rotate(0,0, rotate6);
        
        
        Node hex1 = new Node("sheep1"); //add 6 triangles to hex node
        Node hex2 = new Node("sheep2");
        Node hex3 = new Node("sheep3");
        Node hex4 = new Node("sheep4");
        Node hex5 = new Node("wheat1");
        Node hex6 = new Node("wheat2");
        Node hex7 = new Node("wheat3");
        Node hex8 = new Node("wheat4");
        Node hex9 = new Node("lumber1");
        Node hex10 = new Node("lumber2");
        Node hex11 = new Node("lumber3");
        Node hex12 = new Node("lumber4");
        Node hex13 = new Node("brick1");
        Node hex14 = new Node("brick2");
        Node hex15 = new Node("brick3");
        Node hex16 = new Node("ore1");
        Node hex17 = new Node("ore2");
        Node hex18 = new Node("ore3");
        Node hex19 = new Node("desert");
        

        //rotate each hex accordingly
        hex1.rotate(0,0,hexRotate);
        hex2.rotate(0,0,hexRotate);
        hex3.rotate(0,0,hexRotate);
        hex4.rotate(0,0,hexRotate);
        hex5.rotate(0,0,hexRotate);
        hex6.rotate(0,0,hexRotate);
        hex7.rotate(0,0,hexRotate);
        hex8.rotate(0,0,hexRotate);
        hex9.rotate(0,0,hexRotate);
        hex10.rotate(0,0,hexRotate);
        hex11.rotate(0,0,hexRotate);
        hex12.rotate(0,0,hexRotate);
        hex13.rotate(0,0,hexRotate);
        hex14.rotate(0,0,hexRotate);
        hex15.rotate(0,0,hexRotate);
        hex16.rotate(0,0,hexRotate);
        hex17.rotate(0,0,hexRotate);
        hex18.rotate(0,0,hexRotate);
        hex19.rotate(0,0,hexRotate);
        
        //place each hex on board
        //atach triangles to each hex
        //sheep 1
        hex1.attachChild(tri11);
        hex1.attachChild(tri12);
        hex1.attachChild(tri13);
        hex1.attachChild(tri14);
        hex1.attachChild(tri15);
        hex1.attachChild(tri16);
        //sheep 2
        hex2.attachChild(tri21);
        hex2.attachChild(tri22);
        hex2.attachChild(tri23);
        hex2.attachChild(tri24);
        hex2.attachChild(tri25);
        hex2.attachChild(tri26);
        //sheep 3
        hex3.attachChild(tri31);
        hex3.attachChild(tri32);
        hex3.attachChild(tri33);
        hex3.attachChild(tri34);
        hex3.attachChild(tri35);
        hex3.attachChild(tri36);
        //sheep 4
        hex4.attachChild(tri41);
        hex4.attachChild(tri42);
        hex4.attachChild(tri43);
        hex4.attachChild(tri44);
        hex4.attachChild(tri45);
        hex4.attachChild(tri46);
        //wheat 1
        hex5.attachChild(tri51);
        hex5.attachChild(tri52);
        hex5.attachChild(tri53);
        hex5.attachChild(tri54);
        hex5.attachChild(tri55);
        hex5.attachChild(tri56);
        //wheat 2
        hex6.attachChild(tri61);
        hex6.attachChild(tri62);
        hex6.attachChild(tri63);
        hex6.attachChild(tri64);
        hex6.attachChild(tri65);
        hex6.attachChild(tri66);
        //wheat 3
        hex7.attachChild(tri71);
        hex7.attachChild(tri72);
        hex7.attachChild(tri73);
        hex7.attachChild(tri74);
        hex7.attachChild(tri75);
        hex7.attachChild(tri76);
        //wheat 4
        hex8.attachChild(tri81);
        hex8.attachChild(tri82);
        hex8.attachChild(tri83);
        hex8.attachChild(tri84);
        hex8.attachChild(tri85);
        hex8.attachChild(tri86);
        //lumber 1
        hex9.attachChild(tri91);
        hex9.attachChild(tri92);
        hex9.attachChild(tri93);
        hex9.attachChild(tri94);
        hex9.attachChild(tri95);
        hex9.attachChild(tri96);
        //lumber 2
        hex10.attachChild(tri101);
        hex10.attachChild(tri102);
        hex10.attachChild(tri103);
        hex10.attachChild(tri104);
        hex10.attachChild(tri105);
        hex10.attachChild(tri106);
        //lumber 3
        hex11.attachChild(tri111);
        hex11.attachChild(tri112);
        hex11.attachChild(tri113);
        hex11.attachChild(tri114);
        hex11.attachChild(tri115);
        hex11.attachChild(tri116);
        //lumber 4
        hex12.attachChild(tri121);
        hex12.attachChild(tri122);
        hex12.attachChild(tri123);
        hex12.attachChild(tri124);
        hex12.attachChild(tri125);
        hex12.attachChild(tri126);
        //brick 1
        hex13.attachChild(tri131);
        hex13.attachChild(tri132);
        hex13.attachChild(tri133);
        hex13.attachChild(tri134);
        hex13.attachChild(tri135);
        hex13.attachChild(tri136);
        //brick 2
        hex14.attachChild(tri141);
        hex14.attachChild(tri142);
        hex14.attachChild(tri143);
        hex14.attachChild(tri144);
        hex14.attachChild(tri145);
        hex14.attachChild(tri146);
        //brick 3
        hex15.attachChild(tri151);
        hex15.attachChild(tri152);
        hex15.attachChild(tri153);
        hex15.attachChild(tri154);
        hex15.attachChild(tri155);
        hex15.attachChild(tri156);
        //ore 1
        hex16.attachChild(tri161);
        hex16.attachChild(tri162);
        hex16.attachChild(tri163);
        hex16.attachChild(tri164);
        hex16.attachChild(tri165);
        hex16.attachChild(tri166);
        //ore 2
        hex17.attachChild(tri171);
        hex17.attachChild(tri172);
        hex17.attachChild(tri173);
        hex17.attachChild(tri174);
        hex17.attachChild(tri175);
        hex17.attachChild(tri176);
        //ore 3
        hex18.attachChild(tri181);
        hex18.attachChild(tri182);
        hex18.attachChild(tri183);
        hex18.attachChild(tri184);
        hex18.attachChild(tri185);
        hex18.attachChild(tri186);
        //desert
        hex19.attachChild(tri191);
        hex19.attachChild(tri192);
        hex19.attachChild(tri193);
        hex19.attachChild(tri194);
        hex19.attachChild(tri195);
        hex19.attachChild(tri196);
        
        /*
        //move hexes to correct position
        //row 1
        //hex1 at origin
        hex1.move(0,0,0);
        hex2.move(1.732f,0,0); //0.866*2
        hex3.move(-1.732f,0,0);
        //row 2
        hex4.move(-2.598f,1.5f,0); //1.732+.866
        hex5.move(-0.866f,1.5f,0);
        hex6.move(0.866f,1.5f,0);
        hex7.move(2.598f,1.5f,0);
        //row 3
        hex8.move(3.464f,3,0); 
        hex9.move(1.732f,3,0);
        hex10.move(0,3,0);
        hex11.move(-1.732f,3,0);
        hex12.move(-3.464f,3,0);
        //row 4
        hex13.move(-2.595f, 4.5f, 0);
        hex14.move(-0.866f, 4.5f, 0);
        hex15.move(0.866f, 4.5f, 0);
        hex16.move(2.595f, 4.5f, 0);
        //row 5
        hex17.move(1.732f,6,0);
        hex18.move(0,6,0);
        hex19.move(-1.732f,6,0);
        */
        
        //shuffle hexes
        shuffle.shuffleArray(indexArray);
        //put at assigned coordiantes
        for (int i = 0; i < indexArray.length; i++) {
        
            hex1.move(xArray[indexArray[i]],yArray[indexArray[i]],0);
            System.out.println("hex"+i+" @ "+indexArray[i]+" x: "+xArray[indexArray[i]]+" y: "+yArray[indexArray[i]]);
            System.out.println("hex"+i+" @ "+indexArray[i]+" value: "+hexValue[indexArray[i]]);
            i++;
            
            hex2.move(xArray[indexArray[i]],yArray[indexArray[i]],0);
            System.out.println("hex"+i+" @ "+indexArray[i]+" x: "+xArray[indexArray[i]]+" y: "+yArray[indexArray[i]]);
            System.out.println("hex"+i+" @ "+indexArray[i]+" value: "+hexValue[indexArray[i]]);
            i++;
            
            hex3.move(xArray[indexArray[i]],yArray[indexArray[i]],0);
            System.out.println("hex"+i+" @ "+indexArray[i]+" x: "+xArray[indexArray[i]]+" y: "+yArray[indexArray[i]]);
            System.out.println("hex"+i+" @ "+indexArray[i]+" value: "+hexValue[indexArray[i]]);
            i++;
            
            hex4.move(xArray[indexArray[i]],yArray[indexArray[i]],0);
            System.out.println("hex"+i+" @ "+indexArray[i]+" x: "+xArray[indexArray[i]]+" y: "+yArray[indexArray[i]]);
            System.out.println("hex"+i+" @ "+indexArray[i]+" value: "+hexValue[indexArray[i]]);
            i++;
            
            hex5.move(xArray[indexArray[i]],yArray[indexArray[i]],0);
            System.out.println("hex"+i+" @ "+indexArray[i]+" x: "+xArray[indexArray[i]]+" y: "+yArray[indexArray[i]]);
            System.out.println("hex"+i+" @ "+indexArray[i]+" value: "+hexValue[indexArray[i]]);
            i++;
            
            hex6.move(xArray[indexArray[i]],yArray[indexArray[i]],0);
            System.out.println("hex"+i+" @ "+indexArray[i]+" x: "+xArray[indexArray[i]]+" y: "+yArray[indexArray[i]]);
            System.out.println("hex"+i+" @ "+indexArray[i]+" value: "+hexValue[indexArray[i]]);
            i++;
            
            hex7.move(xArray[indexArray[i]],yArray[indexArray[i]],0);
            System.out.println("hex"+i+" @ "+indexArray[i]+" x: "+xArray[indexArray[i]]+" y: "+yArray[indexArray[i]]);
            System.out.println("hex"+i+" @ "+indexArray[i]+" value: "+hexValue[indexArray[i]]);
            i++;
            
            hex8.move(xArray[indexArray[i]],yArray[indexArray[i]],0);
            System.out.println("hex"+i+" @ "+indexArray[i]+" x: "+xArray[indexArray[i]]+" y: "+yArray[indexArray[i]]);
            System.out.println("hex"+i+" @ "+indexArray[i]+" value: "+hexValue[indexArray[i]]);
            i++;
            
            hex9.move(xArray[indexArray[i]],yArray[indexArray[i]],0);
            System.out.println("hex"+i+" @ "+indexArray[i]+" x: "+xArray[indexArray[i]]+" y: "+yArray[indexArray[i]]);
            System.out.println("hex"+i+" @ "+indexArray[i]+" value: "+hexValue[indexArray[i]]);
            i++;
            
            hex10.move(xArray[indexArray[i]],yArray[indexArray[i]],0);
            System.out.println("hex"+i+" @ "+indexArray[i]+" x: "+xArray[indexArray[i]]+" y: "+yArray[indexArray[i]]);
            System.out.println("hex"+i+" @ "+indexArray[i]+" value: "+hexValue[indexArray[i]]);
            i++;
            
            hex11.move(xArray[indexArray[i]],yArray[indexArray[i]],0);
            System.out.println("hex"+i+" @ "+indexArray[i]+" x: "+xArray[indexArray[i]]+" y: "+yArray[indexArray[i]]);
            System.out.println("hex"+i+" @ "+indexArray[i]+" value: "+hexValue[indexArray[i]]);
            i++;
            
            hex12.move(xArray[indexArray[i]],yArray[indexArray[i]],0);
            System.out.println("hex"+i+" @ "+indexArray[i]+" x: "+xArray[indexArray[i]]+" y: "+yArray[indexArray[i]]);
            System.out.println("hex"+i+" @ "+indexArray[i]+" value: "+hexValue[indexArray[i]]);
            i++;
            
            hex13.move(xArray[indexArray[i]],yArray[indexArray[i]],0);
            System.out.println("hex"+i+" @ "+indexArray[i]+" x: "+xArray[indexArray[i]]+" y: "+yArray[indexArray[i]]);
            System.out.println("hex"+i+" @ "+indexArray[i]+" value: "+hexValue[indexArray[i]]);
            i++;
            
            hex14.move(xArray[indexArray[i]],yArray[indexArray[i]],0);
            System.out.println("hex"+i+" @ "+indexArray[i]+" x: "+xArray[indexArray[i]]+" y: "+yArray[indexArray[i]]);
            System.out.println("hex"+i+" @ "+indexArray[i]+" value: "+hexValue[indexArray[i]]);
            i++;
            
            hex15.move(xArray[indexArray[i]],yArray[indexArray[i]],0);
            System.out.println("hex"+i+" @ "+indexArray[i]+" x: "+xArray[indexArray[i]]+" y: "+yArray[indexArray[i]]);
            System.out.println("hex"+i+" @ "+indexArray[i]+" value: "+hexValue[indexArray[i]]);
            i++;
            
            hex16.move(xArray[indexArray[i]],yArray[indexArray[i]],0);
            System.out.println("hex"+i+" @ "+indexArray[i]+" x: "+xArray[indexArray[i]]+" y: "+yArray[indexArray[i]]);
            System.out.println("hex"+i+" @ "+indexArray[i]+" value: "+hexValue[indexArray[i]]);
            i++;
            
            hex17.move(xArray[indexArray[i]],yArray[indexArray[i]],0);
            System.out.println("hex"+i+" @ "+indexArray[i]+" x: "+xArray[indexArray[i]]+" y: "+yArray[indexArray[i]]);
            System.out.println("hex"+i+" @ "+indexArray[i]+" value: "+hexValue[indexArray[i]]);
            i++;
            
            hex18.move(xArray[indexArray[i]],yArray[indexArray[i]],0);
            System.out.println("hex"+i+" @ "+indexArray[i]+" x: "+xArray[indexArray[i]]+" y: "+yArray[indexArray[i]]);
            System.out.println("hex"+i+" @ "+indexArray[i]+" value: "+hexValue[indexArray[i]]);
            i++;
            
            hex19.move(xArray[indexArray[i]],yArray[indexArray[i]],0); //desert does not have value
            System.out.println("hex"+i+" @ "+indexArray[i]+" x: "+xArray[indexArray[i]]+" y: "+yArray[indexArray[i]]);
            
        } //end for
        
        
        //attach each hex to board
        board.attachChild(hex1);
        board.attachChild(hex2);
        board.attachChild(hex3);
        board.attachChild(hex4);
        board.attachChild(hex5);
        board.attachChild(hex6);
        board.attachChild(hex7);
        board.attachChild(hex8);
        board.attachChild(hex9);
        board.attachChild(hex10);
        board.attachChild(hex11);
        board.attachChild(hex12);
        board.attachChild(hex13);
        board.attachChild(hex14);
        board.attachChild(hex15);
        board.attachChild(hex16);
        board.attachChild(hex17);
        board.attachChild(hex18);
        board.attachChild(hex19);
        
        board.rotate(floorRotate,0,0); //tilt
        //
        board.move(0,-0.75f,4); //put in better camera view 
        //with water
        //board.move( -327.21957f, 100f, 126.884346f);
       
        rootNode.attachChild(board);
        
    } //end make hexes
    
    
    public void makeRoads() {
        //house shape
        Box road11 = new Box(0.05f, 0.4f, .05f);
        redRoad1 = new Geometry("road11", road11);
        redRoad2 = new Geometry("road12", road11);
 
        //settlement materials
        Material whiteSett = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        whiteSett.setColor("Color", ColorRGBA.White);  //settle1
        
        redRoad1.setMaterial(whiteSett);
        redRoad2.setMaterial(whiteSett);
        
        redRoad1.move(0.7f,-0.05f,0.1f); 
        //redRoad2.move(-0.866f,0.5f,0.1f);
        
        Node roads = new Node("roads");
        
        roads.attachChild(redRoad1);
        //roads.attachChild(redRoad2);
        
        float roadRotate = FastMath.DEG_TO_RAD *120f;
        
        roads.rotate(0,0,roadRotate);
        
        board.attachChild(roads);

    }
    
    public void makeModels() {
        //model dimensions 0.2*0.2*0.4
        settlement1_1 = assetManager.loadModel("Models/settlement/settlement.obj");
        settlement1_2 = assetManager.loadModel("Models/settlement/settlement.obj");
        settlement1_3 = assetManager.loadModel("Models/settlement/settlement.obj");
        settlement1_4 = assetManager.loadModel("Models/settlement/settlement.obj");
        settlement1_5 = assetManager.loadModel("Models/settlement/settlement.obj");
        
        float houseRotate = FastMath.DEG_TO_RAD *90f;
        settlement1_1.rotate(houseRotate,0,0);
        settlement1_2.rotate(houseRotate,0,0);
        settlement1_3.rotate(houseRotate,0,0);
        settlement1_4.rotate(houseRotate,0,0);
        settlement1_5.rotate(houseRotate,0,0);
        
        DirectionalLight sun = new DirectionalLight();
        Vector3f sunlight = new Vector3f(-0.5f,-0.5f,-0.5f);
        sun.setDirection(sunlight);
        sun.setColor(ColorRGBA.White);
        
        board.attachChild(settlements);
        rootNode.addLight(sun);
    }
    
    public void makeDice() {
        AmbientLight al = new AmbientLight();
        rootNode.addLight(al);

        DirectionalLight dl = new DirectionalLight();
        dl.setDirection(Vector3f.UNIT_XYZ.negate());
        rootNode.addLight(dl);

        Box box = new Box(0.25f, 0.25f, 0.25f);

        // Setup bone weight buffer
        FloatBuffer weights = FloatBuffer.allocate( box.getVertexCount() * 4 );
        VertexBuffer weightsBuf = new VertexBuffer(Type.BoneWeight);
        weightsBuf.setupData(VertexBuffer.Usage.CpuOnly, 4, VertexBuffer.Format.Float, weights);
        box.setBuffer(weightsBuf);

        // Setup bone index buffer
        ByteBuffer indices = ByteBuffer.allocate( box.getVertexCount() * 4 );
        VertexBuffer indicesBuf = new VertexBuffer(Type.BoneIndex);
        indicesBuf.setupData(VertexBuffer.Usage.CpuOnly, 4, VertexBuffer.Format.UnsignedByte, indices);
        box.setBuffer(indicesBuf);

        // Create bind pose buffers
        box.generateBindPose(true);

        // Create skeleton
        bone = new Bone("root");
        bone.setBindTransforms(Vector3f.ZERO, Quaternion.IDENTITY, Vector3f.UNIT_XYZ);
        bone.setUserControl(true);
        skeleton = new Skeleton(new Bone[]{ bone });

        // Assign all verticies to bone 0 with weight 1
        for (int i = 0; i < box.getVertexCount() * 4; i += 4){
            // assign vertex to bone index 0
            indices.array()[i+0] = 0;
            indices.array()[i+1] = 0;
            indices.array()[i+2] = 0;
            indices.array()[i+3] = 0;

            // set weight to 1 only for first entry
            weights.array()[i+0] = 1;
            weights.array()[i+1] = 0;
            weights.array()[i+2] = 0;
            weights.array()[i+3] = 0;
        }

        // Maximum number of weights per bone is 1
        box.setMaxNumWeights(1);

        // Create model
        dice1 = new Geometry("box1", box);
        dice2 = new Geometry("box2", box);
        dice1.setMaterial(assetManager.loadMaterial("Materials/dice.j3m"));
        dice2.setMaterial(assetManager.loadMaterial("Materials/dice.j3m"));
        
        Node model = new Node("model");
        model.attachChild(dice1);
        model.attachChild(dice2);
        
        //move model
        dice1.move(-0.5f,12,-2);
        dice2.move(0.5f,12,-2);

        // Create skeleton control
        SkeletonControl skeletonControl = new SkeletonControl(skeleton);
        model.addControl(skeletonControl);

        model.move(0, 4, 0);
        board.attachChild(model);
    }
    
    public void makeGUInode() {
        setDisplayStatView(false);
        setDisplayFps(false);
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        
        diceText = new BitmapText(guiFont);
        diceText.setSize(guiFont.getCharSet().getRenderedSize());
        diceText.move(settings.getWidth()/4, diceText.getLineHeight()*4, 0);
        diceText.setText("You rolled: ");
        
        resourceHexText = new BitmapText(guiFont);
        resourceHexText.setSize(guiFont.getCharSet().getRenderedSize());
        resourceHexText.move(settings.getWidth()/4, diceText.getLineHeight()*3, 0);
        resourceHexText.setText("Collect: ");
        
        resourceListText = new BitmapText(guiFont);
        resourceListText.setSize(guiFont.getCharSet().getRenderedSize());
        resourceListText.move(settings.getWidth()/4, diceText.getLineHeight()*2, 0);
        resourceListText.setText("Resources: ");
        
        WinText = new BitmapText(guiFont);
        WinText.setSize(guiFont.getCharSet().getRenderedSize());
        WinText.move(settings.getWidth()/4, diceText.getLineHeight(), 0);
        
        
        guiNode.attachChild(diceText);
        guiNode.attachChild(resourceHexText);
        guiNode.attachChild(resourceListText);
        guiNode.attachChild(WinText);
        
        Picture  play1 = new Picture("user interface frame");
        play1.setImage(assetManager, "Interface/player1.png", false);
        play1.move(settings.getWidth()/2-265, 0, -2);
        play1.setWidth(30);
        play1.setHeight(30);
        guiNode.attachChild(play1);
    }
      
    //add landscape
    private void createSky() {
        Texture west = assetManager.loadTexture("Textures/Sky/Lagoon/lagoon_west.jpg");
        Texture east = assetManager.loadTexture("Textures/Sky/Lagoon/lagoon_east.jpg");
        Texture north = assetManager.loadTexture("Textures/Sky/Lagoon/lagoon_north.jpg");
        Texture south = assetManager.loadTexture("Textures/Sky/Lagoon/lagoon_south.jpg");
        Texture up = assetManager.loadTexture("Textures/Sky/Lagoon/lagoon_up.jpg");
        Texture down = assetManager.loadTexture("Textures/Sky/Lagoon/lagoon_down.jpg");

        Spatial sky = SkyFactory.createSky(assetManager, west, east, north, south, up, down);
        rootNode.attachChild(sky);
    }
      
    
    //Add moving water
    /*void createWater() {
        Node mainScene = new Node("Main Scene");
        rootNode.attachChild(mainScene);
        createTerrain(mainScene);
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(lightDir);
        sun.setColor(ColorRGBA.White.clone().multLocal(1.7f));
        mainScene.addLight(sun);

        DirectionalLight l = new DirectionalLight();
        l.setDirection(Vector3f.UNIT_Y.mult(-1));
        l.setColor(ColorRGBA.White.clone().multLocal(0.3f));

        //flyCam.setMoveSpeed(10);

        //cam.setLocation(new Vector3f(-700, 100, 300));
        //cam.setRotation(new Quaternion().fromAngleAxis(0.5f, Vector3f.UNIT_Z));
        //cam.setLocation(new Vector3f(-327.21957f, 105f, 134f));
        //cam.setRotation(new Quaternion(0.5f, 0.9443102f, -0.6f, 0.2678024f));
        //cam.setRotation(new Quaternion(0.052168474f, 0.9443102f, -0.18395276f, 0.2678024f));
                                                                        //X-axis            //Y-axis
        //cam.setRotation(new Quaternion().fromAngles(new float[]{FastMath.DEG_TO_RAD*-60f, FastMath.DEG_TO_RAD * 0f, 0}));

        //add landscape
        Spatial sky = SkyFactory.createSky(assetManager, "Scenes/Beach/FullskiesSunset0068.dds", false);
        //sky.setLocalScale(350);

        mainScene.attachChild(sky);
        //cam.setFrustumFar(4000);
        //cam.setFrustumNear(100);



        //private FilterPostProcessor fpp;


        water = new WaterFilter(rootNode, lightDir);

        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);

        fpp.addFilter(water);
        BloomFilter bloom = new BloomFilter();
        //bloom.getE
        bloom.setExposurePower(55);
        bloom.setBloomIntensity(0.1f); //was 1.0
        fpp.addFilter(bloom);
        LightScatteringFilter lsf = new LightScatteringFilter(lightDir.mult(-300));
        lsf.setLightDensity(0.1f); //was 1.0
        fpp.addFilter(lsf);
        DepthOfFieldFilter dof = new DepthOfFieldFilter();
        dof.setFocusDistance(0);
        dof.setFocusRange(50); //was 100
        fpp.addFilter(dof);
        //        
        //   fpp.addFilter(new TranslucentBucketFilter());
        //       

        // fpp.setNumSamples(4);

        water.setWaveScale(0.003f); //was .003
        water.setMaxAmplitude(0.5f);
        water.setFoamExistence(new Vector3f(1f, 4, 0.5f));
        water.setFoamTexture((Texture2D) assetManager.loadTexture("Common/MatDefs/Water/Textures/foam2.jpg"));
        //water.setNormalScale(0.5f);

        //water.setRefractionConstant(0.25f);
        water.setRefractionStrength(0.2f);
        //water.setFoamHardness(0.6f);

        water.setWaterHeight(initialWaterHeight);
        uw = cam.getLocation().y < waterHeight;
*/
    public void createWater(){
        waves = new AudioNode(assetManager, "Sound/Environment/Ocean Waves.ogg", false);
        waves.setLooping(true);
        waves.setReverbEnabled(true);
        
        audioRenderer.playSource(waves);
        //  viewPort.addProcessor(fpp);
        
       // audioRenderer.playSource(waves);
        
} //end create water 
     

    /*private void createTerrain(Node rootNode) {
        matRock = new Material(assetManager, "Common/MatDefs/Terrain/TerrainLighting.j3md");
        matRock.setBoolean("useTriPlanarMapping", false);
        matRock.setBoolean("WardIso", true);
        matRock.setTexture("AlphaMap", assetManager.loadTexture("Textures/Terrain/splat/alphamap.png"));
        Texture heightMapImage = assetManager.loadTexture("Textures/Terrain/splat/mountains512.png");
        Texture grass = assetManager.loadTexture("Textures/Terrain/splat/grass.jpg");
        grass.setWrap(Texture.WrapMode.Repeat);
        matRock.setTexture("DiffuseMap", grass);
        matRock.setFloat("DiffuseMap_0_scale", 64);
        Texture dirt = assetManager.loadTexture("Textures/Terrain/splat/dirt.jpg");
        dirt.setWrap(Texture.WrapMode.Repeat);
        matRock.setTexture("DiffuseMap_1", dirt);
        matRock.setFloat("DiffuseMap_1_scale", 16);
        Texture rock = assetManager.loadTexture("Textures/Terrain/splat/road.jpg");
        rock.setWrap(Texture.WrapMode.Repeat);
        matRock.setTexture("DiffuseMap_2", rock);
        matRock.setFloat("DiffuseMap_2_scale", 128);
        Texture normalMap0 = assetManager.loadTexture("Textures/Terrain/splat/grass_normal.jpg");
        normalMap0.setWrap(Texture.WrapMode.Repeat);
        Texture normalMap1 = assetManager.loadTexture("Textures/Terrain/splat/dirt_normal.png");
        normalMap1.setWrap(Texture.WrapMode.Repeat);
        Texture normalMap2 = assetManager.loadTexture("Textures/Terrain/splat/road_normal.png");
        normalMap2.setWrap(Texture.WrapMode.Repeat);
        matRock.setTexture("NormalMap", normalMap0);
        matRock.setTexture("NormalMap_1", normalMap2);
        matRock.setTexture("NormalMap_2", normalMap2);

        AbstractHeightMap heightmap = null;
        try {
            heightmap = new ImageBasedHeightMap(heightMapImage.getImage(), 0.25f);
            heightmap.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        terrain = new TerrainQuad("terrain", 65, 513, heightmap.getHeightMap());
        List<Camera> cameras = new ArrayList<Camera>();
        cameras.add(getCamera());
        terrain.setMaterial(matRock);
        //terrain.setLocalScale(new Vector3f(5, 5, 5));
        //terrain.setLocalTranslation(new Vector3f(0, -30, 0));
        terrain.setLocked(false); // unlock it so we can edit the height

        terrain.setShadowMode(RenderQueue.ShadowMode.Receive);
        rootNode.attachChild(terrain);

    }
    
    //end Add terrain */
    
    /*Dice roll and resource collection function*/
    public void collectResources() {
        int j = 0;
        for(int i = 0; i < hexValue.length; i++) { //19 hexvalues
            /*hexValue with index indexArray[i], it is essentially
              shuffled in the same order as the hexes, that way the
              hex value points to the correct placement of the hex*/
            if(hexValue[indexArray[i]] == diceSum) { 
                resourceHex[j] = i; //offset of 1
                System.out.println("resourceHex["+j+"]: "+i);
                j++;
            }
        }
        
        
    }
    
    boolean movedSett1 = false;
    boolean movedSett2 = false;
    boolean movedSett3 = false;
    boolean movedSett4 = false;
    boolean movedSett5 = false;
    
    public void placeSettlements() {
        if (movedSett1 == false) {
            settlement1_1.move(newXLocation,newYLocation,0.1f);
            settlements.attachChild(settlement1_1); 
            movedSett1 = true;
            System.out.println("Moved settlement1");
            drum.play();
            
            
        }
        if (movedSett2 == false) {
            settlement1_2.move(newXLocation,newYLocation,0.1f);
            settlements.attachChild(settlement1_2); 
            movedSett2 = true;
            System.out.println("Moved settlement2");
        }
        if (movedSett3 == false) {
            settlement1_3.move(newXLocation,newYLocation,0.1f);
            settlements.attachChild(settlement1_3); 
            movedSett3 = true;
            System.out.println("Moved settlement3");
        }
        if (movedSett4 == false) {
            settlement1_4.move(newXLocation,newYLocation,0.1f);
            settlements.attachChild(settlement1_4); 
            movedSett4 = true;
            System.out.println("Moved settlement4");
        }
        if (movedSett5 == false) {
            settlement1_5.move(newXLocation,newYLocation,0.1f);
            settlements.attachChild(settlement1_5); 
            movedSett5 = true;
            System.out.println("Moved settlement5");
            WinText.setText("You Won!");
        }
       
        
        System.out.println("MOVING SETTLEMENT");
        
    }
    
    //ACTION & ANALOG LISTENERS
    
    //Anonymous declaration
    //a key is mapped to name of type of action
    //MAPPING_COLOR1 is mapped to key 'x'
    public ActionListener actionListener = new ActionListener() {
        public void onAction(String name, boolean isPressed, float tpf) {
            if (name.equals(MAPPING_COLOR1) && !isPressed) {
                redRoad1.getMaterial().setColor("Color", ColorRGBA.randomColor());
            }
            if (name.equals(MAPPING_COLOR2) && !isPressed) {
                redRoad2.getMaterial().setColor("Color", ColorRGBA.randomColor());
            }
            if (name.equals(MAPPING_COLOR3) && !isPressed) {
                redRoad3.getMaterial().setColor("Color", ColorRGBA.randomColor());
            }
            
            //roll dice
            if (name.equals(MAPPING_ROLL) && !isPressed) {
                
                //roll geometry
                dice1.rotate(0.9f, 0, 0);
                dice2.rotate( 0,0.9f,0);
                //randomize the roll value
                shuffle.shuffleArray(diceArr);

                diceSum = diceArr[0]+diceArr[1];
                //if 7 is rolled then robber comes
                if(diceSum == 7) {
                    System.out.println("The robber has come");
                    diceText.setText("You rolled: 7");
                    resourceHexText.setText("Collect: (from robber placement)");
                    resource1text = ""; //reset
                    resource2text = "";
                    resourceListText.setText("Resources: (You've been robbed!)");
                    //remove all resources from array and display
                    for(int i = 0; i < 28; i++) {
                        resourcesP1[i] = "";
                    }
                    rsrcCounterP1 = 0; //reset
                }
                //if dice = anything else then collect those resources if you are allowed to
                else {
                    //call collect resources function to store resources after every roll
                    collectResources();
                    //update dice display
                    System.out.println("The dice rolled: "+diceSum);
                    System.out.println("resourcehex0: "+resourceHex[0]+"  resourcehex1: "+resourceHex[1]); //debug
                    diceText.setText("You rolled: "+diceSum+"     @ places "+indexArray[resourceHex[0]]+" and "
                                                                            +indexArray[resourceHex[1]]);
                    //update resource display
                    
                    if(resourceHex[0] == 1 || resourceHex[0] ==2 || resourceHex[0] ==3 || resourceHex[0] ==4) {
                        resource1text = "sheep"; //to display what can be collected
                        resourcesP1[rsrcCounterP1] = resource1text;
                        rsrcCounterP1++;
                        resourceListText.setText("Resources - 1: "+resourcesP1[0]+" 2: "+resourcesP1[1]+"3: "+resourcesP1[2]+"4: "+resourcesP1[3]+"5: "+resourcesP1[4]+"6: "+resourcesP1[5]+"7: "+resourcesP1[6]);
                    }
                    else if(resourceHex[0] == 5 || resourceHex[0] == 6 || resourceHex[0] == 7 || resourceHex[0] == 8)
                    {resource1text = "wheat";
                     resourcesP1[rsrcCounterP1] = resource1text;
                     rsrcCounterP1++;
                     resourceListText.setText("Resources - 1: "+resourcesP1[0]+" 2: "+resourcesP1[1]+"3: "+resourcesP1[2]+"4: "+resourcesP1[3]+"5: "+resourcesP1[4]+"6: "+resourcesP1[5]+"7: "+resourcesP1[6]);
                    
                    }
                    else if(resourceHex[0] == 9 || resourceHex[0] == 10 || resourceHex[0] == 11 || resourceHex[0] == 12)
                    {resource1text = "lumber";
                     resourcesP1[rsrcCounterP1] = resource1text;
                     rsrcCounterP1++;
                     resourceListText.setText("Resources - 1: "+resourcesP1[0]+" 2: "+resourcesP1[1]+"3: "+resourcesP1[2]+"4: "+resourcesP1[3]+"5: "+resourcesP1[4]+"6: "+resourcesP1[5]+"7: "+resourcesP1[6]);
                    
                    }
                    else if(resourceHex[0] == 13 || resourceHex[0] == 14 || resourceHex[0] == 15) {
                     resource1text = "brick";
                     resourcesP1[rsrcCounterP1] = resource1text;
                     rsrcCounterP1++;
                     resourceListText.setText("Resources - 1: "+resourcesP1[0]+" 2: "+resourcesP1[1]+"3: "+resourcesP1[2]+"4: "+resourcesP1[3]+"5: "+resourcesP1[4]+"6: "+resourcesP1[5]+"7: "+resourcesP1[6]);
                       
                    }
                    else if(resourceHex[0] == 16 || resourceHex[0] == 17 || resourceHex[0] == 18) {
                     resource1text = "ore";
                     resourcesP1[rsrcCounterP1] = resource1text;
                     rsrcCounterP1++;
                     resourceListText.setText("Resources - 1: "+resourcesP1[0]+" 2: "+resourcesP1[1]+"3: "+resourcesP1[2]+"4: "+resourcesP1[3]+"5: "+resourcesP1[4]+"6: "+resourcesP1[5]+"7: "+resourcesP1[6]);
                       
                    }

                    if(resourceHex[1] == 1 || resourceHex[1] ==2 || resourceHex[1] ==3 || resourceHex[1] ==4) {
                        resource2text = "sheep";
                        resourcesP1[rsrcCounterP1] = resource2text;
                        rsrcCounterP1++;
                        resourceListText.setText("Resources - 1: "+resourcesP1[0]+" 2: "+resourcesP1[1]+"3: "+resourcesP1[2]+"4: "+resourcesP1[3]+"5: "+resourcesP1[4]+"6: "+resourcesP1[5]+"7: "+resourcesP1[6]);
                    
                    }
                    else if(resourceHex[1] == 5 || resourceHex[1] == 6 || resourceHex[1] == 7 || resourceHex[1] == 8)
                    {   resource2text = "wheat";
                        resourcesP1[rsrcCounterP1] = resource2text;
                        rsrcCounterP1++;
                        resourceListText.setText("Resources - 1: "+resourcesP1[0]+" 2: "+resourcesP1[1]+"3: "+resourcesP1[2]+"4: "+resourcesP1[3]+"5: "+resourcesP1[4]+"6: "+resourcesP1[5]+"7: "+resourcesP1[6]);
                    
                    }
                    else if(resourceHex[1] == 9 || resourceHex[1] == 10 || resourceHex[1] == 11 || resourceHex[1] == 12)
                    {   resource2text = "lumber";
                        resourcesP1[rsrcCounterP1] = resource2text;
                        rsrcCounterP1++;
                        resourceListText.setText("Resources - 1: "+resourcesP1[0]+" 2: "+resourcesP1[1]+"3: "+resourcesP1[2]+"4: "+resourcesP1[3]+"5: "+resourcesP1[4]+"6: "+resourcesP1[5]+"7: "+resourcesP1[6]);
                    
                    }
                    else if(resourceHex[1] == 13 || resourceHex[1] == 14 || resourceHex[1] == 15)
                    {resource2text = "brick";
                     resourcesP1[rsrcCounterP1] = resource2text;
                     rsrcCounterP1++;
                     resourceListText.setText("Resources - 1: "+resourcesP1[0]+" 2: "+resourcesP1[1]+"3: "+resourcesP1[2]+"4: "+resourcesP1[3]+"5: "+resourcesP1[4]+"6: "+resourcesP1[5]+"7: "+resourcesP1[6]);
                    
                    }
                    else if(resourceHex[1] == 16 || resourceHex[1] == 17 || resourceHex[1] == 18)
                    {   resource2text = "ore";
                        resourcesP1[rsrcCounterP1] = resource2text;
                        rsrcCounterP1++;
                        resourceListText.setText("Resources - 1: "+resourcesP1[0]+" 2: "+resourcesP1[1]+"3: "+resourcesP1[2]+"4: "+resourcesP1[3]+"5: "+resourcesP1[4]+"6: "+resourcesP1[5]+"7: "+resourcesP1[6]);
                    
                    }


                    resourceHexText.setText("Collect: "+resource1text+" and "+resource2text);
                }
                
                //*for debugging resource allocation
                for(int i = 0; i < 28; i++) {
                    System.out.println("resourcesP1["+i+"]: "+resourcesP1[i]);
                } //*/
                
                //check if player can add settlements or roads
                boolean sheepChecker = false;
                boolean wheatChecker = false;
                boolean lumberChecker = false;
                boolean brickChecker = false;
                boolean oreChecker = false;
                
                for(int i = 0; i < 28; i++) {
                    if(resourcesP1[i].equals("sheep"))
                        sheepChecker = true;
                    else if(resourcesP1[i].equals("wheat"))
                        wheatChecker = true;
                    else if(resourcesP1[i].equals("lumber"))
                        lumberChecker = true;
                    else if(resourcesP1[i].equals("brick"))
                        brickChecker = true;
                    else if(resourcesP1[i].equals("ore"))
                        oreChecker = true;
                }
                
                if(sheepChecker == true && wheatChecker == true && lumberChecker == true && brickChecker == true) {
                    placement = true;
                }
                else {
                    placement = false;
                }
                
            } //end if "roll" is activated
            
            
            if(name.equals(MAPPING_PLACEMENT) && !isPressed) { 
            
            CollisionResults results = new CollisionResults();
            
            //get 2d coordinates of mouse
            Vector2f click2d = inputManager.getCursorPosition();
            //convert to 3d
            Vector3f click3d = cam.getWorldCoordinates(new Vector2f(click2d.getX(), click2d.getY()), 0f);
            //cast ray forward to depth of screen
            Vector3f dir = cam.getWorldCoordinates(new Vector2f(click2d.getX(), click2d.getY()), 1f).subtractLocal(click3d);
            //aim from click location to calculated forward direction
            Ray ray = new Ray(click3d, dir);
            //ray.setDirection(dir);
            rootNode.collideWith(ray, results);
            
            //need to decclare contactpt in for loop because this is an analog listener 
            //and when you click button the computer registers multiple clicks and the index changes
            //if not inside for loop then you get outofboundsexception b/c index changes during click
            for(int i = 0; i < results.size(); i++) {
                contactpt = results.getCollision(i).getContactPoint();
            }
            
            //if you clicked something and youre allowed to place an object...
            //&&placement
            if (results.size() > 0 && placement) {
                Geometry target = results.getClosestCollision().getGeometry();
                //make vector for contact point
                //System.out.println("RESULTS SIZE: "+results.size());
                //place it by scaling and rounding to nearest value
                //System.out.println("target: "+target+ " contactptx: "+contactpt.x + "  contactpointy: "+contactpt.y +
                  //      "  scaledpointy: "+(2*(0.75+contactpt.y)));
                /*scale it by translating screen location to board location
                  board is tilted at 60 degree angle, so every screen click is 
                  0.75 + 2 times off desired placment // 0.75 is original board movement       */
                //round x coords by taking closest multiple of 0.866
                //round y coords by taking closet multiple of 0.5
                float xDiv = 0.866f;
                float yDiv = 0.5f;
                float half = 0.5f;
                
                float yLocations[] = {-1,-0.5f,0.5f,1,2,2.5f,3.5f,4,5,5.5f,6.5f,7};
                //water offset -327.21957f, 105f, 134f
                if(contactpt.x%xDiv <half) {
                    newXLocation = (float) (Math.floor(contactpt.x/xDiv)*xDiv) ;
                    System.out.println("move x to "+newXLocation);
                }
                else {
                    newXLocation = (float) (Math.ceil(contactpt.x/xDiv)*xDiv);
                    System.out.println("move x to = "+newXLocation);
                }
                
                float min = 100;
                int minIndex = 0;
                float result = 0;

                for(int k = 0; k < yLocations.length; k++) {
                    result = Math.abs(yLocations[k] - (2f*(0.75f+contactpt.y)));
                    //System.out.println("res: "+result); 
                    if(result < min) {
                        min = result;
                        minIndex = k;
                    }
                    //System.out.println("min: "+min+"   minIdex: "+minIndex); 

                }
                newYLocation = yLocations[minIndex];
                System.out.println("move y to = "+newYLocation);
                
                
                placeSettlements();          
            
            } //end if results size
            } //end if left clcik
            
        } //end onAction
    };
    
    
    
    private AnalogListener analogListener = new AnalogListener() {
        public void onAnalog(String name, float intensity, float tpf) {
            //System.out.println("You triggered: " + name);
            
            
        } //end onAnalog
    }; //end analog listener
    
    

    //This part is to emulate tides, slightly varrying the height of the water plane
    private float time = 0.0f;
    private float waterHeight = 0.0f;
    private float initialWaterHeight = 90f;//0.8f;
    private boolean uw = false;
    
    
    //interact with update loop here
    @Override
    public void simpleUpdate(float tpf) {
        //Animated object rotates contantly
        // Rotate around X axis
        /*ROTATE DICE
        Quaternion rotate = new Quaternion();
        rotate.fromAngleAxis(tpf, Vector3f.UNIT_X);
        // Combine rotation with previous
        rotation.multLocal(rotate);
        // Set new rotation into bone
        bone.setUserTransforms(Vector3f.ZERO, rotation, Vector3f.UNIT_XYZ);
        // After changing skeleton transforms, must update world data
        skeleton.updateWorldVectors();
        */
        
        //guiNode
        //distance = Vector3f.ZERO.distance(cam.getLocation());
        //distanceText.setText("Distance: "+distance);
        
        
        /*/water
        super.simpleUpdate(tpf);
        time += tpf;
        waterHeight = (float) Math.cos(((time * 0.6f) % FastMath.TWO_PI)) * 1.5f;
        water.setWaterHeight(initialWaterHeight + waterHeight);
        if (water.isUnderWater() && !uw) {

            waves.setDryFilter(new LowPassFilter(0.5f, 0.1f));
            uw = true;
        }
        if (!water.isUnderWater() && uw) {
            uw = false;
            //waves.setReverbEnabled(false);
            waves.setDryFilter(new LowPassFilter(1, 1f));
            //waves.setDryFilter(new LowPassFilter(1,1f));

        } //end water */
    } 
//end simple update
    
    //advanced render/frameBuffer modification
    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
