package loader3DS;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

public class Scalling extends Transform {

	public float x;
	public float y;
	public float z;
	private Matrix4f orientation;
	private Vector3f translation;

	public Scalling(float x, float y, float z,Matrix4f orientation,Vector3f translation) {
		this.x=x;
		this.y=y;
		this.z=z;
		this.orientation = orientation;
		this.translation = translation;
	}

	@Override
	public Object getMatrix() {
		Matrix4f        m           = new Matrix4f();
        //m.set( orientation );
        //m.setTranslation( translation );
		
        return m;
	}
	
	public String toString(){
		return "Scalling";
	}
}
