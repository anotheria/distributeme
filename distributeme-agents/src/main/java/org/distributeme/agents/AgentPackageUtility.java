package org.distributeme.agents;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AgentPackageUtility {
	public static AgentPackage pack(Agent agent){
		AgentPackage ret = new AgentPackage();
	
		try{
			ret.setRootClazzName(agent.getClass().getName());
			ret.setSerializedData(serializeAgent(agent));
			List<Class<?>> classes = scanForCustomClasses(agent);
			for (Class<?> c : classes){
				ret.addClazzDefinition(c.getName(), readClass(c));
			}
		}catch(IOException e){
			throw new RuntimeException("Couldn't pack agent "+agent, e);
		}
		return ret;
	}
	
	private static byte[] readClass(Class<?> c){
		InputStream in = null;
		try{
			String path = c.getSimpleName()+".class";
			in = c.getResourceAsStream(path);
			byte[] clazzData = new byte[in.available()];
			in.read(clazzData);
			return clazzData;
		}catch(IOException e){
			throw new RuntimeException("Couldn't load class "+c, e);
		}finally{
			if (in!=null){
				try{
					in.close();
				}catch(IOException ignored){}
			}
		}
	}
	
	public static Agent unpack(AgentPackage pack){
		try{
			AgentPackageUtilClassLoader loader = new AgentPackageUtilClassLoader(pack);
			//force loading
			Class myAgent = Class.forName(pack.getRootClazzName(), true, loader);
			Agent agent = deserializeAgent(pack.getSerializedData(), loader);
			return agent;
		}catch (Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	static class AgentPackageUtilClassLoader extends ClassLoader{
		private Map<String, byte[]> classDefs;
		
		private Map<String, Class> cache = new HashMap<>();;
		
		public AgentPackageUtilClassLoader(AgentPackage pack) {
			classDefs = pack.getClazzDefinitions();
		}
		
		protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException{
//			System.out.println("-- loadClass "+name);
			Class fromCache = cache.get(name);
			if (fromCache!=null){
//				System.out.println("--> from cache! ");
				return fromCache;
			}
			if (classDefs.get(name)==null){
				return super.loadClass(name, resolve);
			}
//			System.out.println(" -- defining "+name);
			byte[] data = classDefs.get(name);
			Class c = defineClass(name, data, 0, data.length);
			cache.put(name, c);
			return c;
		}

	}
	
	private static byte[] serializeAgent(Agent obj) throws IOException{
		ByteArrayOutputStream bOut = null;
		ObjectOutputStream oOut = null;
		try{
			bOut = new ByteArrayOutputStream();
			oOut = new ObjectOutputStream(bOut);
			oOut.writeObject(obj);
			return bOut.toByteArray();
		}finally{ 
			if (oOut!=null)
				try{
					oOut.close();
				}catch(Exception ignored){}//NOPMD
		}
	}
	
	private static Agent deserializeAgent(final byte[] data, final AgentPackageUtilClassLoader loader) throws IOException{
		ByteArrayInputStream bIn = null;
		ObjectInputStream oIn = null;
		try{
			bIn = new ByteArrayInputStream(data);
			oIn = new ObjectInputStream(bIn){

				@Override
				protected Class<?> resolveClass(ObjectStreamClass desc)
						throws IOException, ClassNotFoundException {
					
					System.out.println("Resolve class "+desc.getName()+" with loader "+loader);
					return loader.loadClass(desc.getName(), false);

				}
				
			};
			return (Agent)oIn.readObject();
		}catch(ClassNotFoundException e){
			throw new IOException("deserialization failed: "+e.getMessage());
		}finally{
			if (oIn!=null){
				try{
					oIn.close();
				}catch(Exception ignored){}
			}
		}
	}
	
	static List<Class<?>> scanForCustomClasses(Agent agent){
		
		List<Class<?>> ret = new ArrayList<>();
		
		scanForCustomClassesInternally(agent.getClass(), ret);
		
		return ret;
	}
	
	private static void scanForCustomClassesInternally(Class<?> toScan, List<Class<?>> foundClasses){
		
		if (toScan.isPrimitive())
			return;
		
		foundClasses.add(toScan);
		
		Field[] fields = toScan.getDeclaredFields();
		if (fields==null || fields.length==0)
			return;
		
		for (Field f : fields){
			if (f.getType().isPrimitive()){
				continue;
			}
			if (f.getType().getName().startsWith("java.")){
				continue;
			}
			
			
			if (!foundClasses.contains(f.getType()))
				scanForCustomClassesInternally(f.getType(), foundClasses);
		}
	}

}
