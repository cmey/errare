package loader3DS;

public class Quaternion {

		public float w,x,y,z;
	
		public Quaternion(float w,float x,float y,float z)
		{
			this.w=w;
			this.x=x;
			this.y=y;
			this.z=z;
		}

		public Quaternion(float[] matrix)
		{
			float T=matrix[0]+matrix[5]+matrix[10]+1;
		
			if ( T > 0 )
			{
				float S=0.5f/(float)Math.sqrt(T);
				w=0.25f/S;
				x=(matrix[9]-matrix[6])*S;
				y=(matrix[2]-matrix[8])*S;
				z=(matrix[4]-matrix[1])*S;
			}
			else
			{
				float S;
			
				if ( matrix[0] > matrix[5] && matrix[0] > matrix[10] )
				{
					S=(float)Math.sqrt(1.0+matrix[0]-matrix[5]-matrix[10])*2;
					x=0.5f/S;
					y=(matrix[1]+matrix[4])/S;
					z=(matrix[2]+matrix[8])/S;
					w=(matrix[6]+matrix[9])/S;

					return;
				}
			
				if ( matrix[5] > matrix[0] && matrix[5] > matrix[10] )
				{
					S=(float)Math.sqrt(1.0+matrix[5]-matrix[0]-matrix[10])*2;
					x=(matrix[1]+matrix[4])/S;
					y=0.5f/S;
					z=(matrix[6]+matrix[9])/S;
					w=(matrix[2]+matrix[8])/S;

					return;
				}
			
				if ( matrix[10] > matrix[0] && matrix[10] > matrix[5] )
				{
					S=(float)Math.sqrt(1.0+ matrix[10]-matrix[0]-matrix[5])*2;
					x=(matrix[2]+ matrix[8])/S;
					y=(matrix[6]+ matrix[9])/S;
					z=0.5f/S;
					w=(matrix[1]+matrix[4])/S;
				
					return;
				}
			}
		}

		public Quaternion multiply(Quaternion quat4f)
		{
			double f2 = w * quat4f.w - x * quat4f.x - y * quat4f.y - z * quat4f.z;
			double f = (w * quat4f.x + quat4f.w * x + y * quat4f.z) - z * quat4f.y;
			double f1 = ((w * quat4f.y + quat4f.w * y) - x * quat4f.z) + z * quat4f.x;
			float zz = (float)((w * quat4f.z + quat4f.w * z + x * quat4f.y) - y * quat4f.x);
			float ww = (float)f2;
			float xx = (float)f;
			float yy = (float)f1;
		
			return new Quaternion(ww,xx,yy,zz);
		}

		public Quaternion interpolate(double f,Quaternion quat4f)
		{
			double d=x*quat4f.x+y*quat4f.y+z*quat4f.z+w*quat4f.w;
			if(d < 0.0D)
			{
				quat4f.x=-quat4f.x;
				quat4f.y=-quat4f.y;
				quat4f.z=-quat4f.z;
				quat4f.w=-quat4f.w;
				d=-d;
			}
			double d1;
			double d2;
			if(1.0D - Math.abs(d) > 9.9999999999999995E-007D)
			{
				double d3=Math.acos(d);
				double d4=Math.sin(d3);
				d1=Math.sin((1.0D-(double)f)*d3)/d4;
				d2=Math.sin((double)f*d3)/d4;
			} 
			else
			{
				d1=1.0D-(double)f;
				d2=f;
			}
		
			Quaternion ret=new Quaternion(0,0,0,0);
			ret.w=(float)(d1*(double)w+d2*(double)quat4f.w);
			ret.x=(float)(d1*(double)x+d2*(double)quat4f.x);
			ret.y=(float)(d1*(double)y+d2*(double)quat4f.y);
			ret.z=(float)(d1*(double)z+d2*(double)quat4f.z);
		
			return ret;
		}
	
		public float[] getMatrix()
		{
			float[] m=new float[16];

			m[0]=(float)(1-2*y*y-2*z*z);
			m[1]=(float)(2*x*y-2*z*w);
			m[2]=(float)(2*x*z+2*y*w);

			m[4]=(float)(2*x*y+2*z*w);
			m[5]=(float)(1-2*x*x-2*z*z);
			m[6]=(float)(2*y*z-2*x*w);

			m[8]=(float)(2*x*z-2*y*w);
			m[9]=(float)(2*y*z+2*x*w);
			m[10]=(float)(1-2*x*x-2*y*y);
		

			m[3]=m[7]=m[11]=m[12]=m[13]=m[14]=0;
			m[15]=1;
		
			return m;
		}
		
		public void inverse()
		{
			double f = 1.0 / (w * w + x * x + y * y + z * z);
			w *= f;
			x *= -f;
			y *= -f;
			z *= -f;
		}

		public void multiplyInverse(Quaternion q)
		{
			Quaternion quat4f1=clone();
			quat4f1.inverse();
			multiply(quat4f1);
		}

		public void conjugate()
		{
			x = -x;
			y = -y;
			z = -z;
		}

		public Quaternion clone()
		{
			return new Quaternion(w,x,y,z);
		}
	
}
