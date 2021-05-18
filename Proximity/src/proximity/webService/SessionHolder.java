package proximity.webService;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Random;

public class SessionHolder {
	private HashMap<String, Integer> sessionsSI;
	private HashMap<Integer, String> sessionsIS;
	private Random random;
	
	public SessionHolder(){
		sessionsSI = new HashMap<String, Integer>();
		sessionsIS = new HashMap<Integer, String>();
		random = new Random();
	}
	
	public String store(int userId){
		String s;
		if((s = sessionsIS.get(userId)) != null){
			return s;
		}
		s = generateSessionKey();
		//s = userId+"";
		sessionsIS.put(userId, s);
		sessionsSI.put(s, userId);
		return s;
	}
	
	public void remove(String sessionId){
		if(sessionsSI.containsKey(sessionId)){
			int userId = sessionsSI.get(sessionId);
			sessionsSI.remove(sessionId);
			sessionsIS.remove(userId);
		}
	}
	
	public int retrieve(String sessionKey){
		if(sessionsSI.containsKey(sessionKey)){
			return sessionsSI.get(sessionKey);
		}
		return -1;
	}
	
	private String generateSessionKey(){
		String name;
		byte[] bytes = new byte[25];
		do {
			random.nextBytes(bytes);
			name = new BigInteger(1, bytes).toString(16);
		} while (sessionsSI.containsKey(name));
		return name;
	}
}
