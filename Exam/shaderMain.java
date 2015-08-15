//
// shaderMain.java
//
// Main class for lighting / shading assignment.
//
// Students should not be modifying this file.
//

import java.awt.*;
import java.nio.*;
import java.awt.event.*;
import javax.media.opengl.*;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.fixedfunc.*;
import com.jogamp.opengl.util.Animator;

public class shaderMain implements GLEventListener, KeyListener
{

    /**
     * We need four vertex buffers and four element buffers:
     * two for the torus (flat shading and non-flat shading) and
     * two for the teapot (flat shading and non-flat shading).
     *
     * Array layout:
     *         column 0      column 1
     * row 0:  torus flat    torus non-flat
     * row 1:  teapot flat   teapot non-flat
     */
    private int vbuffer[][];
    private int ebuffer[][];
    private int numVerts[][];

    /**
     * Animation control
     */
    Animator anime;
    boolean animating;

    /**
     * Initial animation rotation angles
     */
    float angles[];

    /**
     * Current shader type:  flat vs. non-flat
     */
    int currentShader;

    /**
     * Program IDs - current, and all variants
     */
    public int program;
    public int flat;
    public int phong;
    public int gouraud;

    /**
     * Shape info
     */
    shapes myShape;

    /**
     * Lighting information
     */
    lightingParams myPhong;

    /**
     * Viewing information
     */
    viewParams myView;

    /**
     * My canvas
     */
    GLCanvas myCanvas;

    /**
     * Constructor
     */
    public shaderMain( GLCanvas G )
    {
        vbuffer = new int[5][2];
        ebuffer = new int[5][2];
        numVerts = new int[5][2];

        angles = new float[2];

        animating = false;
        currentShader = shapes.SHADE_FLAT;

        angles[0] = 0.0f;
        angles[1] = 0.0f;

        myCanvas = G;

        // Initialize lighting and view
        myPhong = new lightingParams();
        myView = new viewParams();

        // Set up event listeners
        G.addGLEventListener (this);
        G.addKeyListener (this);
    }

    private void errorCheck (GL2 gl2)
    {
        int code = gl2.glGetError();
        if (code == GL.GL_NO_ERROR)
            System.err.println ("All is well");
        else
            System.err.println ("Problem - error code : " + code);

    }


    /**
     * Simple animate function
     */
    public void animate() {
        angles[shapes.OBJ_CUBE_1]  += 2;
        angles[shapes.OBJ_CUBE_2] += 1;
        angles[shapes.OBJ_CUBE_3] += 1;
        angles[shapes.OBJ_CUBE_4] += 1;
        angles[shapes.OBJ_CYl] += 1;
        angles[shapes.OBJ_SPHERE] += 1;
        angles[shapes.OBJ_CONE] += 1;
    }

    /**
     * Called by the drawable to initiate OpenGL rendering by the client.
     */
    public void display(GLAutoDrawable drawable)
    {
        // get GL
        GL2 gl2 = (drawable.getGL()).getGL2();

        // clear and draw params..
        gl2.glClear( GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT );

        // use the correct program
        gl2.glUseProgram( program );

        // set up Phong illumination
        
        myPhong.setUpPhong( program, gl2,0,0,0.4f);
        // set up viewing and projection parameters
        myView.setUpFrustum( program, gl2 );

        // set up the camera
        myView.setUpCamera( program, gl2,
        	0.f, 0.0f, 6f,
            0.0f, 0.0f, 3f,
            0.0f, 1.0f, 0.0f
        );

        // left wing
        // set up transformations for the torus
        myView.setUpTransforms( program, gl2,
            1f, 0.5f, 2f,
            -40,40,30,
            0.1f, 0.4f, 1.8f
        );       
        selectBuffers( gl2, shapes.OBJ_CUBE_1, currentShader );
        gl2.glDrawElements( GL.GL_TRIANGLES,
            numVerts[shapes.OBJ_CUBE_1][currentShader],
            GL.GL_UNSIGNED_SHORT, 0l
        );
       
        
        // right wing
        myView.setUpTransforms( program, gl2,
                1f, 0.5f, 2f,
                -40,40,30,
                1.2f, 0.5f,1.3f
            );       
            selectBuffers( gl2, shapes.OBJ_CUBE_1, currentShader );
            gl2.glDrawElements( GL.GL_TRIANGLES,
                numVerts[shapes.OBJ_CUBE_1][currentShader],
                GL.GL_UNSIGNED_SHORT, 0l
            );
            
            myPhong.setUpPhong( program,gl2,0.8f,0.89f,1f);
            myView.setUpTransforms( program, gl2,
            		0.01f, 0.37f, 2f,
            		-40,40,30,
                    0.35f, 0.42f, 3f
                );
            
            selectBuffers( gl2, shapes.OBJ_CUBE_4, currentShader );
            gl2.glDrawElements( GL.GL_TRIANGLES,
                numVerts[shapes.OBJ_CUBE_4][currentShader],
                GL.GL_UNSIGNED_SHORT, 0l
            );
        	 
            
            myView.setUpTransforms( program, gl2,
            		0.01f, 0.39f, 2f,
            		-40,40,30,
                    0.15f, 0.42f, 3f
                );
            
            selectBuffers( gl2, shapes.OBJ_CUBE_4, currentShader );
            gl2.glDrawElements( GL.GL_TRIANGLES,
                numVerts[shapes.OBJ_CUBE_4][currentShader],
                GL.GL_UNSIGNED_SHORT, 0l
            );
        	
            myView.setUpTransforms( program, gl2,
            		0.01f, 0.35f, 2f,
            		-40,40,30,
                    0.95f, 0.49f, 3f
                );
            
            selectBuffers( gl2, shapes.OBJ_CUBE_4, currentShader );
            gl2.glDrawElements( GL.GL_TRIANGLES,
                numVerts[shapes.OBJ_CUBE_4][currentShader],
                GL.GL_UNSIGNED_SHORT, 0l
            );
        	
            myView.setUpTransforms( program, gl2,
            		0.01f, 0.33f, 2f,
            		-40,40,30,
                    1.1f, 0.50f, 3f
                );
            
            selectBuffers( gl2, shapes.OBJ_CUBE_4, currentShader );
            gl2.glDrawElements( GL.GL_TRIANGLES,
                numVerts[shapes.OBJ_CUBE_4][currentShader],
                GL.GL_UNSIGNED_SHORT, 0l
            );
        	
            
       //cone
            
       myPhong.setUpPhong( program,gl2,1f,1f,1f);
            
       myView.setUpTransforms( program, gl2,
            		0.35f, 0, 0.4f,
            		-40,-20,20,
            		0f, 0.15f, 2f
            		);
       
            
            selectBuffers( gl2, shapes.OBJ_CONE, currentShader );
            gl2.glDrawElements( GL.GL_TRIANGLES,
                    numVerts[shapes.OBJ_CONE][currentShader],
                    GL.GL_UNSIGNED_SHORT, 0l
                );

        
        
        //cylinder
        myPhong.setUpPhong( program,gl2,1.1f,0.84f,0.2f);

        myView.setUpTransforms( program, gl2,
        		0.5f, 1f, 0.5f,
        		-10,-60,220,
        		0f, 0f,1f
        		);

        
        selectBuffers( gl2, shapes.OBJ_CYl, currentShader );
        gl2.glDrawElements( GL.GL_TRIANGLES,
                numVerts[shapes.OBJ_CYl][currentShader],
                GL.GL_UNSIGNED_SHORT, 0l
            );

        
        // Sphere
        
        
        myPhong.setUpPhong( program,gl2,0.4f,0,0);
        myView.setUpTransforms( program, gl2,
        		4.5f, 4.5f, 4.5f,
        		220,20,0,
        		0f, 0f, -2f
        		);

        
        selectBuffers( gl2, shapes.OBJ_SPHERE, currentShader );
        gl2.glDrawElements( GL.GL_TRIANGLES,
                numVerts[shapes.OBJ_SPHERE][currentShader],
                GL.GL_UNSIGNED_SHORT, 0l
            );

        
        
        
        // perform any required animation for the next time
        if( animating ) {
            animate();
        }
    }

    /**
     * Notifies the listener to perform the release of all OpenGL
     * resources per GLContext, such as memory buffers and GLSL
     * programs.
     */
    public void dispose(GLAutoDrawable drawable)
    {
    }

    /**
     * Verify shader creation
     */
    private void checkShaderError( shaderSetup myShaders, int program,
        String which )
    {
        if( program == 0 ) {
            System.err.println( "Error setting " + which +
                " shader - " +
                myShaders.errorString(myShaders.shaderErrorCode)
            );
            System.exit( 1 );
        }
    }

    /**
     * Called by the drawable immediately after the OpenGL context is
     * initialized.
     */
    public void init(GLAutoDrawable drawable)
    {
        // get the gl object
        GL2 gl2 = drawable.getGL().getGL2();

        // create the Animator now that we have the drawable
        anime = new Animator( drawable );

        // Load shaders, verifying each
        shaderSetup myShaders = new shaderSetup();

        flat = myShaders.readAndCompile( gl2, "flat.vert", "flat.frag" );
        checkShaderError( myShaders, flat, "flat" );

        gouraud = myShaders.readAndCompile(gl2,"gouraud.vert","gouraud.frag");
        checkShaderError( myShaders, gouraud, "gouraud" );

        phong = myShaders.readAndCompile( gl2, "phong.vert", "phong.frag" );
        checkShaderError( myShaders, phong, "phong" );

        // Default shader program
        program = flat;

        // Create all four shapes
        createShape( gl2, shapes.OBJ_CUBE_1, shapes.SHADE_FLAT );
        createShape( gl2, shapes.OBJ_CUBE_1, shapes.SHADE_NOT_FLAT );
        createShape( gl2, shapes.OBJ_CUBE_2, shapes.SHADE_FLAT );
        createShape( gl2, shapes.OBJ_CUBE_2, shapes.SHADE_NOT_FLAT );
        createShape( gl2, shapes.OBJ_CYl, shapes.SHADE_FLAT );
        createShape( gl2, shapes.OBJ_CYl, shapes.SHADE_NOT_FLAT );
        createShape( gl2, shapes.OBJ_CUBE_3, shapes.SHADE_FLAT );
        createShape( gl2, shapes.OBJ_CUBE_3, shapes.SHADE_NOT_FLAT );
        createShape( gl2, shapes.OBJ_CUBE_4, shapes.SHADE_FLAT );
        createShape( gl2, shapes.OBJ_CUBE_4, shapes.SHADE_NOT_FLAT );
        createShape( gl2, shapes.OBJ_SPHERE, shapes.SHADE_FLAT );
        createShape( gl2, shapes.OBJ_SPHERE, shapes.SHADE_NOT_FLAT );
        createShape( gl2, shapes.OBJ_CONE, shapes.SHADE_FLAT );
        createShape( gl2, shapes.OBJ_CONE, shapes.SHADE_NOT_FLAT );
       
        
        
        // Other GL initialization
        gl2.glEnable( GL.GL_DEPTH_TEST );
        gl2.glEnable( GL.GL_CULL_FACE );
        gl2.glCullFace(  GL.GL_BACK );
        gl2.glFrontFace( GL.GL_CCW );
        gl2.glClearColor( 0.0f, 0.0f, 0.0f, 0.0f );
        gl2.glDepthFunc( GL.GL_LEQUAL );
        gl2.glClearDepth( 5.0f );
    }


    /**
     * Called by the drawable during the first repaint after the component
     * has been resized.
     */
    public void reshape(GLAutoDrawable drawable, int x, int y, int width,
                     int height)
    {
    }


    /**
     * Create vertex and element buffers for a shape
     */
    public void createShape(GL2 gl2, int obj, int flat )
    {
        // clear the old shape
        myShape = new shapes();

        // make the shape
        myShape.makeShape( obj, flat );

        // save the vertex count
        numVerts[obj][flat] = myShape.nVertices();

        // get the vertices
        Buffer points = myShape.getVertices();
        long dataSize = myShape.nVertices() * 4l * 4l;

        // get the normals
        Buffer normals = myShape.getNormals();
        long ndataSize = myShape.nVertices() * 3l * 4l;

        // get the element data
        Buffer elements = myShape.getElements();
        long edataSize = myShape.nVertices() * 2l;

        // generate the vertex buffer
        int bf[] = new int[1];

        gl2.glGenBuffers( 1, bf, 0 );
        vbuffer[obj][flat] = bf[0];
        gl2.glBindBuffer( GL.GL_ARRAY_BUFFER, vbuffer[obj][flat] );
        gl2.glBufferData( GL.GL_ARRAY_BUFFER, dataSize + ndataSize, null,
        GL.GL_STATIC_DRAW );
        gl2.glBufferSubData( GL.GL_ARRAY_BUFFER, 0, dataSize, points );
        gl2.glBufferSubData( GL.GL_ARRAY_BUFFER, dataSize, ndataSize,
        normals );

        // generate the element buffer
        gl2.glGenBuffers (1, bf, 0);
        ebuffer[obj][flat] = bf[0];
        gl2.glBindBuffer( GL.GL_ELEMENT_ARRAY_BUFFER, ebuffer[obj][flat] );
        gl2.glBufferData( GL.GL_ELEMENT_ARRAY_BUFFER, edataSize, elements,
            GL.GL_STATIC_DRAW );

    }

    /**
     * Bind the correct vertex and element buffers
     *
     * Assumes the correct shader program has already been enabled
     */
    private void selectBuffers( GL2 gl2, int obj, int flat )
    {
        // bind the buffers
        gl2.glBindBuffer( GL.GL_ARRAY_BUFFER, vbuffer[obj][flat] );
        gl2.glBindBuffer( GL.GL_ELEMENT_ARRAY_BUFFER, ebuffer[obj][flat] );

        // calculate the number of bytes of vertex data
        long dataSize = numVerts[obj][flat] * 4l * 4l;

        // set up the vertex attribute variables
        int vPosition = gl2.glGetAttribLocation( program, "vPosition" );
        gl2.glEnableVertexAttribArray( vPosition );
        gl2.glVertexAttribPointer( vPosition, 4, GL.GL_FLOAT, false,
                                       0, 0l );
        int vNormal = gl2.glGetAttribLocation( program, "vNormal" );
        gl2.glEnableVertexAttribArray( vNormal );
        gl2.glVertexAttribPointer( vNormal, 3, GL.GL_FLOAT, false,
                                   0, dataSize );

    }

    /**
     * Because I am a Key Listener...we'll only respond to key presses
     */
    public void keyTyped(KeyEvent e){}
    public void keyReleased(KeyEvent e){}

    /**
     * Invoked when a key has been pressed.
     */
    public void keyPressed(KeyEvent e)
    {
        // Get the key that was pressed
        char key = e.getKeyChar();

        // Respond appropriately
        switch( key ) {

            case '1':    // flat shading
                program = flat;
                currentShader = shapes.SHADE_FLAT;
                break;

            case '2':    // Gouraud shading
                program = gouraud;
                currentShader = shapes.SHADE_NOT_FLAT;
                break;

            case '3':    // phong shading
                program = phong;
                currentShader = shapes.SHADE_NOT_FLAT;
                break;

            case 'a':    // animate
                animating = true;
                anime.start();
                break;

            case 's':    // stop animating
                animating = false;
                anime.stop();
                break;

            case 'q': case 'Q':
                System.exit( 0 );
                break;
        }

        // do a redraw
        myCanvas.display();
    }


    /**
     * main program
     */
    public static void main(String [] args)
    {
        // GL setup
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);
        GLCanvas canvas = new GLCanvas(caps);

        // create your tessMain
        shaderMain myMain = new shaderMain(canvas);


        Frame frame = new Frame("CG - Shading Assignment");
        frame.setSize(900, 600);
        frame.add(canvas);
        frame.setVisible(true);

        // by default, an AWT Frame doesn't do anything when you click
        // the close button; this bit of code will terminate the program when
        // the window is asked to close
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }
}
