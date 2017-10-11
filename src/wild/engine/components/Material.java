package wild.engine.components;

import org.newdawn.slick.opengl.Texture;

import wild.engine.maths.Vec3;

public class Material
{
        private Texture texture;
        private Vec3 color;
        private float specularIntensity;
        private float specularPower;
        
        public Material(Texture texture)
        {
                this(texture, new Vec3(1,1,1));
        }
        
        public Material(Texture texture, Vec3 color)
        {
                this(texture, color, 2, 32);
        }
        
        public Material(Texture texture, Vec3 color, float specularIntensity, float specularPower)
        {
                this.texture = texture;
                this.color = color;
                this.specularIntensity = specularIntensity;
                this.specularPower = specularPower;
        }

        public Texture getTexture()
        {
                return texture;
        }

        public void setTexture(Texture texture)
        {
                this.texture = texture;
        }

        public Vec3 getColor()
        {
                return color;
        }

        public void setColor(Vec3 color)
        {
                this.color = color;
        }

        public float getSpecularIntensity()
        {
                return specularIntensity;
        }

        public void setSpecularIntensity(float specularIntensity)
        {
                this.specularIntensity = specularIntensity;
        }

        public float getSpecularPower()
        {
                return specularPower;
        }

        public void setSpecularPower(float specularPower)
        {
                this.specularPower = specularPower;
        }
}
