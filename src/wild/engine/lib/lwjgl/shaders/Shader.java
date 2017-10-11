package wild.engine.lib.lwjgl.shaders;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform2f;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUniform4f;
import static org.lwjgl.opengl.GL20.glUniformMatrix4;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glValidateProgram;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.lwjgl.util.vector.Vector4f;

import wild.engine.maths.Buffers;
import wild.engine.maths.Mat4;
import wild.engine.maths.Vec2;
import wild.engine.maths.Vec3;
import wild.utils.RGB;

public class Shader {
	//public static final Shader AMBIANT_LIGHT = new Shader("res/shaders/lights/ambient_light.vert", "res/shaders/lights/ambient_light.frag");
    //public static final Shader DIRECTIONAL_LIGHT = new Shader("res/shaders/lights/directional_light.vert", "res/shaders/lights/directional_light.frag");
	
	public static final Shader MAIN = new Shader("res/shaders/main.vs", "res/shaders/main.fs");

    public int program;

    public Shader(String vertex, String fragment)
    {
        createShaderPrograms(vertex, fragment);
    }

    public int getUniform(String name)
    {
        return glGetUniformLocation(program, name);
    }

    public void setInt(int location, int v)
    {
        glUniform1i(location, v);
    }

    public void setFloat(int location, float v)
    {
        glUniform1f(location, v);
    }

    public void setVec2(int location, Vec2 v)
    {
        glUniform2f(location, v.x, v.y);
    }

    public void setVec3(int location, Vec3 v)
    {
        glUniform3f(location, v.x, v.y, v.z);
    }

    public void setVec4(int location, Vector4f v)
    {
        glUniform4f(location, v.x, v.y, v.z, v.w);
    }

    public void setColor4f(int location, RGB v)
    {
        glUniform4f(location, v.r, v.g, v.b, v.a);
    }

    public void set2f(int location, float x, float y)
    {
        glUniform2f(location, x, y);
    }

    public void set3f(int location, float x, float y, float z)
    {
        glUniform3f(location, x, y, z);
    }

    public void set4f(int location, float x, float y, float z, float w)
    {
        glUniform4f(location, x, y, z, w);
    }

    public void setMat4(int location, Mat4 v)
    {
        glUniformMatrix4(location, true, Buffers.toMatrixBuffer(v));
    }

    public void bind()
    {
        glUseProgram(program);
    }

    public void unbind()
    {
        glUseProgram(0);
    }

    private void createShaderPrograms(String vertex, String fragment)
    {
        program = glCreateProgram();

        if (program == GL_FALSE)
        {
            System.err.println("Shader program error");
            System.exit(1);
        }

        createShader(loadShader(vertex), GL_VERTEX_SHADER);
        createShader(loadShader(fragment), GL_FRAGMENT_SHADER);

        glLinkProgram(program);
        glValidateProgram(program);
    }

    private void createShader(String source, int type)
    {
        int shader = glCreateShader(type);
        if (shader == GL_FALSE)
        {
            System.err.println("Shader error: " + shader);
            System.exit(1);
        }
        
        glShaderSource(shader, source);
        glCompileShader(shader);
        if (glGetShaderi(shader, GL_COMPILE_STATUS) == GL_FALSE)
        {
            System.err.println("####################################################\n"
            				+ "Shader ERROR :\n"
            				+ "########################################## FILE --> \n"
            				+ source.trim() + "\n"
            				+ "####################################################\n"
            				+ "Error information : (" + glGetShaderInfoLog(shader, 2048).replaceAll("\\n", "") + ")\n"
            				+ "####################################################");
            System.exit(1);
        }
        glAttachShader(program, shader);
    }

    private static String loadShader(String shaderFile)
    {
        long beforeTime = System.nanoTime();
        final String INCLUDE_FUNC = "#include";
        String r = "";
        System.out.print("Loading shader: " + shaderFile + "... ");
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(shaderFile));
            String buffer = "";
            while ((buffer = reader.readLine()) != null)
            {
                if (buffer.startsWith(INCLUDE_FUNC))
                {
                    String[] fileDir = shaderFile.split("/");
                    String dir = shaderFile.substring(0, shaderFile.length() - fileDir[fileDir.length - 1].length());
                    r += loadShader(dir + buffer.substring(INCLUDE_FUNC.length() + 2, buffer.length() - 1));
                } else
                {
                    r += buffer + "\n";
                }
            }
            reader.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        System.out.println("Done in " + ((System.nanoTime() - beforeTime) / 1000000.0) + "ms !");
        return r;
    }
}
