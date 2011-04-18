/*Errare is a free and crossplatform MMORPG project.
Copyright (C) 2006  Christophe MEYER

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.*/

package graphicsEngine;
   import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;


   interface Constants
   {
    //colors
      int rgbColor=0x0010;
      int rgbColor24bit=0x0011;
      int rgbColorOnTop=0x0012;
      int rgbColorUnknown=0x0013;
      int amountOf=0x0030;
      int configuration=0x0100;
      int materialUnknownName=0x1100;
      int backgroundColor=0x1200;
    //color
      int ambientColor=0x2100;
    //editor tags
      int editor=0x3d3d;
      int editorConfiguration=0x3d3e;
    //
      int object=0x4000;
      int shadow=0x4012;
      int triangularPolygonList=0x4100;
      int vertexList=0x4110;
      int vertexOptions=0x4111;
      int faceList=0x4120;
      int faceMaterial=0x4130;
      int mappingCoordinates=0x4140;
      int faceSmoothingGroup=0x4150;
      int translationMatrix=0x4160;
      int objectVisible=0x4165;
      int standardMapping=0x4170;
      int light=0x4600;
      int lightOff=0x4620;
      int spotLight=0x4610;
      int lightUnknownTag1=0x465a;
      int camera=0x4700;
    //main
      int main=0x4d4d;
    //viewport
      int viewport=0x7001;
      int viewportType1=0x7012;
      int viewportType2=0x7011;
      int viewportType3=0x7020;
    //material tags
      int material=0xafff;
      int materialName=0xa000;
      int materialAmbientColor=0xa010;
      int diffuseColor=0xa020;
      int specularColor=0xa030;
      int shininess=0xa040;
      int shininessStrength=0xa041;
      int transparency=0xa050;
      int transparencyFalloff=0xa052;
      int reflectionBlur=0xa053;
      int materialType=0xa100;
      int selfIllumination=0xa084;
    //optional material tags
      int someTransparencyFalloff=0xa240;
      int someReflectionBlur=0xa250;
      int displayedAmountOfBump=0xa252;
      int twoSided=0xa081;
      int transparencyADD=0xa083;
      int wireOn=0xa085;
      int faceMap=0xa088;
      int transparencyFalloffIN=0xa08a;
      int soften=0xa08c;
      int wireThicknessInUnits=0xa08e;
    //
      int wireThickness=0xa087;
    //map & mask
      int mapTexture1=0xa200;
      int mapSpecular=0xa204;
      int mapOpacity=0xa210;
      int mapReflection=0xa220;
      int mapBump=0xa230;
      int fileName=0xa300;
      int automaticReflection=0xa310;
      int mapTexture2=0xa33a;
      int mapShininess=0xa33c;
      int mapSelfIllumination=0xa33d;
      int maskTexture1=0xa33e;
      int maskTexture2=0xa340;
      int maskOpacity=0xa342;
      int maskBump=0xa344;
      int maskSpecular=0xa348;
      int maskShininess=0xa346;
      int maskSelfIllumination=0xa34a;
      int maskReflection=0xa34c;
   
      int mapOptions=0xa351;
      int mapFilteringBlur=0xa353;
      int mapScaleU=0xa354;
      int mapScaleV=0xa356;
      int mapOffsetU=0xa358;
      int mapOffsetV=0xa35a;
      int mapRotationAngle=0xa35c;
      int alphaTintFirstColor=0xa360;
      int alphaTintSecondColor=0xa362;
      int rgbTintR=0xa364;
      int rgbTintG=0xa366;
      int rgbTintB=0xa368;
    //keyframer tags
      int keyframer=0xb000;
      int frameInfo=0xb002;
      int frameList=0xb008;
      int nameAndHierarchy=0xb010;
      int nameDummyObject=0xb011;
      int pivot=0xb013;
      int keyframerUnknown1=0xb020;
      int nodeId=0xb030;
   }

   abstract class Converter
   {
      public static final int unknown=0;
      public static final int longTriple=1;
      public static final int floatTriple=2;
      public static final int byteRGBColor=3;
      public static final int longRGBColor=4;
      public static final int polygonInfo=5;
   
      public static final char commaChar=',';
      public static final char nlChar='\n';
   
      public static final HashMap nameMap;
   
      static
      {
         nameMap=Converter.createNameMap();
      }
   
      protected static final StringBuffer sb=new StringBuffer();
   
      public Converter()
      {
         super();
      }
      protected static HashMap createNameMap()
      {
         final HashMap nameMap=new HashMap();
      
         Class c=null;
         Field[] array=null;
         int i,k;
      
         try
         {
            c=Constants.class;
            array=c.getDeclaredFields();
            for(i=0;i<array.length;i++)
            {
               k=array[i].getInt(c);
               nameMap.put(Integer.toHexString(k),array[i].getName());
            }
         }
            catch(Throwable x)
            {
               x.printStackTrace();
            }
      
         return nameMap;
      }
      public static String toString(Vertex vertex)
      {
         if(vertex==null)
            return null;
         sb.delete(0,sb.length());
         sb.append("X=");
         sb.append(vertex.array[0]);
         sb.append(",Y=");
         sb.append(vertex.array[1]);
         sb.append(",Z=");
         sb.append(vertex.array[2]);
         return sb.toString();
      }
      public static String toString(DataChunk chunk)
      {
         if(chunk==null)
            return null;
         String tag=Integer.toHexString(chunk.tag);
      /*
      tag
      name
      total size
      content size
      */
         sb.delete(0,sb.length());
         sb.append("<");
         sb.append(chunk.offset);
         sb.append(Converter.commaChar);
         sb.append(tag);
         sb.append(Converter.commaChar);
         sb.append((String)nameMap.get(tag));
         sb.append(Converter.commaChar);
         sb.append(chunk.getSize());
         sb.append(Converter.commaChar);
         sb.append(chunk.getContentSize());
         sb.append(">");
      
         return sb.toString();
      }
      public static String toString(InputContext ic,int flag) throws IOException
      {
         if(ic==null)
            return null;
         sb.delete(0,sb.length());
         switch(flag)
         {
            case Converter.longTriple:
               sb.append(ic.readLong());
               sb.append(Converter.commaChar);
               sb.append(ic.readLong());
               sb.append(Converter.commaChar);
               sb.append(ic.readLong());
               break;
         
            case Converter.floatTriple:
               sb.append(ic.readFloat());
               sb.append(Converter.commaChar);
               sb.append(ic.readFloat());
               sb.append(Converter.commaChar);
               sb.append(ic.readFloat());
               break;
         
            case Converter.byteRGBColor:
               sb.append("R=");
               sb.append(ic.readByte());
               sb.append(",G=");
               sb.append(ic.readByte());
               sb.append(",B=");
               sb.append(ic.readByte());
               break;
         
            case Converter.longRGBColor:
               sb.append("R=");
               sb.append(ic.readFloat());
               sb.append(",G=");
               sb.append(ic.readFloat());
               sb.append(",B=");
               sb.append(ic.readFloat());
               break;
         
            case Converter.polygonInfo:
               sb.append("A=");
               sb.append(ic.readInt());
               sb.append(",B=");
               sb.append(ic.readInt());
               sb.append(",C=");
               sb.append(ic.readInt());
               sb.append(",face info=");
               sb.append(ic.readInt());
               break;
         }
         return sb.toString();
      }
      public static String toString(Face face)
      {
         if(face==null)
            return null;
      
         sb.delete(0,sb.length());
         sb.append("A=");
         sb.append(face.array[0]);
         sb.append(",B=");
         sb.append(face.array[1]);
         sb.append(",C=");
         sb.append(face.array[2]);
         sb.append(",F1=");
         sb.append(face.array[3]);
         sb.append(",F2=");
         sb.append(face.array[4]);
         sb.append(",F3=");
         sb.append(face.array[5]);
         return sb.toString();
      }
   }

   class DataChunk
   {
      public int tag=0;
      public long size=0;
      public long offset=0;
   
      public DataChunk()
      {
         super();
      }
      public long getSize()
      {
         return size;
      }
      public long getContentSize()
      {
         return size-6;
      }
   }

   class Vertex
   {
      public final float[] array=new float[3];//x,y,z
      public final float[] normal = new float[3];
   
      public Vertex()
      {
         super();
      }
      public String toString()
      {
         return Converter.toString(this);
      }
      public void read(InputContext ic) throws IOException
      {
         if(ic==null)
            return;
         for(int i=0;i<array.length;i++)
            array[i]=ic.readFloat();
      }
   }

   class MapCoordonate{
      public float u = 0.0f;
      public float v = 0.0f;
      public MapCoordonate()
      {
         super();
      }
      public void read(InputContext ic) throws IOException
      {
         if(ic==null)
            return;
         u = ic.readFloat();
         v = ic.readFloat();
      }
   
   }

   class Face
   {
      public final int[] array=new int[6];
   
      public Face()
      {
         super();
      }
      public String toString()
      {
         return Converter.toString(this);
      }
      public void read(InputContext ic) throws IOException
      {
         if(ic==null)
            return;
      
         array[0]=ic.readInt();
         array[1]=ic.readInt();
         array[2]=ic.readInt();
         int tmp=ic.readInt()&0x000f;
         array[3]=(tmp&0x0004)>>2;
         array[4]=(tmp&0x0002)>>1;
         array[5]=(tmp&0x0001);
      }
   }

   class DSMesh
   {
      public String name=null;
      public Vertex[] vertexArray = new Vertex[0];
      public Face[] faceArray= new Face[0];
      public MapCoordonate[] mapCoordArray = new MapCoordonate[0];
      public ArrayList materialAppliArray = new ArrayList(0);//MaterialAppli
   
      public DSMesh()
      {
         super();
      }
   }

   class MaterialAppli
   {
      public String name = null;
      public int[] lesFaces = null;
      public MaterialAppli(){
      }
   }


   class Material
   {
      public String name = null;
      public String texture = null;
      public boolean color = false;
      public boolean enByte = false;
      public byte[] RGBb = new byte[3];
      public float[] RGB = new float[3];
   
      public Material()
      {
         super();
      }
   }

   class InputContext
   {
      protected InputStream is=null;
      protected final byte[] buffer=new byte[512];
      protected int dataSize=0;
      protected int index=0;
      protected final int[] intBuffer=new int[2];
      protected final long[] longBuffer=new long[2];
      protected long processedLength=0;
      protected final StringBuffer sb=new StringBuffer();
      protected int tmp=0;
   
      public InputContext(InputStream is)
      {
         super();
         this.is=is;
      }
      protected int readInt() throws IOException
      {
         intBuffer[1]=readByte();
         intBuffer[0]=readByte();
         tmp=0;
         for(int i=0;i<2;i++)
         {
            intBuffer[i]&=0xff;
            tmp<<=8;
            tmp|=intBuffer[i];
         }
         return (int)tmp;
      }
      protected long readLong() throws IOException
      {
         long value=0;
         longBuffer[1]=readInt();
         longBuffer[0]=readInt();
         for(int i=0;i<2;i++)
         {
            longBuffer[i]&=0xffff;
            value<<=16;
            value|=longBuffer[i];
         }
         return value;
      }
      protected double readDouble() throws IOException
      {
         return readLong();
      }
      protected byte readByte() throws IOException
      {
         if(index==dataSize)
         {
            processedLength+=dataSize;
            dataSize=is.read(buffer);
            index=0;
            if(dataSize<=0)
               throw new IOException("EOF");
         }
         return buffer[index++];
      }
      protected long getPosition()
      {
         return processedLength+index;
      }
      protected void readChunk(DataChunk chunk) throws IOException
      {
         if(chunk==null)
            return;
         chunk.offset=getPosition();
         chunk.tag=readInt();
         chunk.size=readLong();
      }
      protected void skip(long value) throws IOException
      {
         long tmp=dataSize-index;
         if(tmp>value)
            index+=value;
         else
         {
            value-=tmp;
            is.skip(value);
            processedLength+=(value+dataSize);
            dataSize=index=0;
         }
      }
      protected String readString(long size) throws IOException
      {
         sb.delete(0,sb.length());
         char c=0;
         for(long i=0;i<size;i++)
         {
            c=(char)readByte();
            sb.append(c);
         }
         return sb.toString();
      }
      protected String readName() throws IOException
      {
         sb.delete(0,sb.length());
         char c=0;
         for(;;)
         {
            c=(char)readByte();
            if(c==0)
               break;
            else
               sb.append(c);
         }
         return sb.toString();
      }
      protected float readFloat() throws IOException
      {
         tmp=(int)readLong();
         return Float.intBitsToFloat(tmp);
      }
   }

   public class DS3Factory
   {
      protected InputContext ic=null;
      protected final ArrayList meshArray=new ArrayList(0);
      protected DSMesh mesh=null;
      protected final ArrayList mapCoordArray = new ArrayList(0);
      protected MapCoordonate mapCoord = null;
      protected Hashtable  materialArray = new Hashtable(0);
      protected Material material = null;//le mat�rial courant
      protected MaterialAppli materialAppli = null;
   
      protected final float[] boundaryArray=new float[6];
      public int objectCount=0;
   
      public ArrayList getMeshArray(){
         return meshArray;
      }
   
      public Hashtable getMaterialArray(){
         return materialArray;
      }
   
      public DS3Factory()
      {
         super();
         for(int i=0;i<boundaryArray.length;i++)
            boundaryArray[i]=0;
      }
      protected void processChunk(DataChunk chunk,InputContext ic) throws IOException
      {
         if(chunk==null || ic==null)
            return;
      
         Converter.toString(chunk);
      
      
         boolean skip=true;
         int i,k,j;
      //		long l;
         double d;
         Vertex vertex=null;
         Face face=null;
      
         switch(chunk.tag)
         {
            case Constants.main:
               skip=false;
               break;
            case Constants.keyframer:
               skip=false;
               break;
            case Constants.frameInfo:
               skip=false;
               break;
            case Constants.nodeId:
               skip=false;
               ic.readInt();
               break;
            case Constants.frameList:
               break;
            case Constants.nameAndHierarchy:
               skip=false;
               ic.readName();
               ic.readLong();
               ic.readInt();
               break;
            case Constants.nameDummyObject:
               break;
            case Constants.pivot:
               skip=false;
               ic.readLong();//x
               ic.readLong();//y
               ic.readLong();//z
               break;
            case Constants.keyframerUnknown1:
               break;
            case Constants.editor:
               skip=false;
               break;
            case Constants.editorConfiguration:
               break;
            case Constants.material:
            ///////////////////////////////////////////////////////////////////////////////////////////////////////MATERIAL
               skip=false;
               if(material != null)
                  materialArray.put(material.name,material);
               material = new Material();
               break;
            case Constants.materialName:
               skip=false;
               material.name = ic.readString(chunk.getContentSize()).trim();
               break;
            case Constants.shininess:
               break;
            case Constants.shininessStrength:
               break;
            case Constants.transparency:
               break;
            case Constants.transparencyFalloff:
               break;
            case Constants.reflectionBlur:
               break;
            case Constants.materialType:
               break;
            case Constants.selfIllumination:
               break;
            case Constants.someTransparencyFalloff:
               break;
            case Constants.someReflectionBlur:
               break;
            case Constants.displayedAmountOfBump:
               break;
            case Constants.twoSided:
               break;
            case Constants.transparencyADD:
               break;
            case Constants.wireOn:
               break;
            case Constants.faceMap:
               break;
            case Constants.transparencyFalloffIN:
               break;
            case Constants.soften:
               break;
            case Constants.wireThicknessInUnits:
               break;
            case Constants.wireThickness:
               break;
            case Constants.configuration:
               break;
         //object
            case Constants.object:
               skip=false;
               ic.readName();
               if(mesh!=null)
                  meshArray.add(mesh);
               mesh=new DSMesh();
               objectCount++;
               break;
         
            case Constants.shadow:
               break;
            case Constants.triangularPolygonList:
               skip=false;
               break;
            case Constants.vertexList:
               skip=false;
               k=ic.readInt();
               mesh.vertexArray=new Vertex[k];
               for(i=0;i<k;i++)
               {
                  vertex=new Vertex();
                  vertex.read(ic);
                  mesh.vertexArray[i]=vertex;
               
                  for(j=0;j<vertex.array.length;j++)
                  {
                     if(vertex.array[j]>boundaryArray[j])
                        boundaryArray[j]=vertex.array[j];
                     if(vertex.array[j]<boundaryArray[j+3])
                        boundaryArray[j+3]=vertex.array[j];
                  }
               }
               break;
            case Constants.vertexOptions:
            
            
               break;
         
            case Constants.faceList:
               skip=false;
               k=ic.readInt();
               mesh.faceArray=new Face[k];
               for(i=0;i<k;i++)
               {
                  face=new Face();
                  face.read(ic);
                  mesh.faceArray[i]=face;
               }
               break;
         
            case Constants.faceMaterial:
               skip=false;
               materialAppli = new MaterialAppli();
               materialAppli.name = ic.readName();
               k=ic.readInt();
               materialAppli.lesFaces = new int[k];
               for(i=0;i<k;i++)
               {
                  materialAppli.lesFaces[i] = ic.readInt();
               }
               mesh.materialAppliArray.add(materialAppli);
               break;
            case Constants.mappingCoordinates:
               skip=false;
               k=ic.readInt();
               mesh.mapCoordArray=new MapCoordonate[k];
               for(i=0;i<k;i++)
               {
                  mapCoord = new MapCoordonate();
                  mapCoord.read(ic);
                  mesh.mapCoordArray[i]=mapCoord;
               }
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
               break;
            case Constants.faceSmoothingGroup:
               break;
            case Constants.translationMatrix:
               skip=false;
               for(i=0;i<4;i++)
               {
                  for(k=0;k<3;k++)
                  {
                     ic.readFloat();
                  }
               }
               break;
            case Constants.objectVisible:
               skip=false;
               ic.readByte();
               break;
            case Constants.standardMapping:
               chunk.size-=2;
               k=ic.readInt();
               break;
         
            case Constants.light:
               skip=false;
               Converter.toString(ic,Converter.longTriple);
               break;
         
            case Constants.lightOff:
               break;
         
            case Constants.spotLight:
               skip=false;
               Converter.toString(ic,Converter.longTriple);
               ic.readLong();
               ic.readLong();
               break;
         
            case Constants.lightUnknownTag1:
               break;
            case Constants.camera:
               break;
            case Constants.viewport:
               break;
            case Constants.viewportType1:
               break;
            case Constants.viewportType2:
               break;
            case Constants.viewportType3:
               break;
            case Constants.backgroundColor:
               skip=false;
               break;
            case Constants.materialUnknownName:
               skip=false;
               ic.readString(chunk.getContentSize());
               break;
         //colors
            case Constants.materialAmbientColor:
            case Constants.ambientColor:
            case Constants.diffuseColor:
            case Constants.specularColor:
               skip=false;
               break;
         
            case Constants.rgbColor:
            case Constants.rgbColorUnknown:
               skip=false;
               if ((material != null) && (!material.color)){
                  material.RGB[0] = ic.readFloat();
                  material.RGB[1] = ic.readFloat();
                  material.RGB[2] = ic.readFloat();
                  material.color = true;
               }
               else Converter.toString(ic,Converter.longRGBColor);
               break;
         
            case Constants.rgbColor24bit:
            case Constants.rgbColorOnTop:
               skip=false;
               if ((material != null) && (!material.color)){
                  material.RGBb[0] = ic.readByte();
                  material.RGBb[1] = ic.readByte();
                  material.RGBb[2] = ic.readByte();
                  material.color = true;
                  material.enByte = true;
               }
               else Converter.toString(ic,Converter.byteRGBColor);
               break;
         
            case Constants.amountOf:
               break;
            case Constants.mapTexture1:
               skip=false;
               break;
            case Constants.mapTexture2:
               break;
            case Constants.mapOpacity:
               break;
            case Constants.mapBump:
               break;
            case Constants.mapSpecular:
               break;
            case Constants.mapShininess:
               break;
            case Constants.mapSelfIllumination:
               break;
            case Constants.mapReflection:
               skip=false;
               break;
            case Constants.maskTexture1:
               skip=false;
               break;
            case Constants.maskTexture2:
               break;
            case Constants.maskOpacity:
               break;
            case Constants.maskBump:
               break;
            case Constants.maskSpecular:
               break;
            case Constants.maskShininess:
               break;
            case Constants.maskSelfIllumination:
               break;
            case Constants.maskReflection:
               break;
            case Constants.fileName:
               skip=false;
               if (material.texture == null){
                  material.texture = ic.readString(chunk.getContentSize()).trim();
               }
               else ic.readString(chunk.getContentSize());
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            
               break;
            case Constants.mapOptions:
               break;
            case Constants.mapFilteringBlur:
               break;
            case Constants.mapScaleU:
               break;
            case Constants.mapScaleV:
               break;
            case Constants.mapOffsetU:
               break;
            case Constants.mapOffsetV:
               break;
            case Constants.mapRotationAngle:
               break;
            case Constants.alphaTintFirstColor:
               break;
            case Constants.alphaTintSecondColor:
               break;
            case Constants.rgbTintR:
               break;
            case Constants.rgbTintG:
               break;
            case Constants.rgbTintB:
               break;
            case Constants.automaticReflection:
               break;
         }
         if(skip)
            ic.skip(chunk.getContentSize());
      }
   
      public void loadData(String fileName)
      {
         if(fileName==null || fileName.length()<1)
            return;
      
         FileInputStream fis=null;
         final DataChunk chunk=new DataChunk();
      
         try
         {
            fis=new FileInputStream(fileName);
            ic=new InputContext(fis);
         
            for(;;)
            {
               ic.readChunk(chunk);
               processChunk(chunk,ic);
            }
         }
            catch(Exception e){
            }
            finally
            {
               if(fis!=null)
                  try
                  {
                     fis.close();
                  }
                     catch(Throwable x)
                     {
                     }
               if(mesh!=null)
                  meshArray.add(mesh);
               if(mapCoord != null)
                  mapCoordArray.add(mapCoord);
               if(material != null)
                  materialArray.put(material.name,material);
            }
      }
      
   }
