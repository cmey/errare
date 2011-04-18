package graphicsEngine;

import genericReps.Rep;

import java.util.List;


import physicsEngine.Octree;

public interface GraphicsEngineAPI {
	
	public void run();
	
	public void setOctree(Octree octree);
	
	public void setPickingManager(PickingManager pickingManager);
	
	public interface PickingManager {
		public void setPickList(List<Rep> pickList);
	}
	

}
