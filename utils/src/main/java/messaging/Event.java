package messaging;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Event {

	private String eventType;
	private Object[] arguments = null;
	
	public Event() {};
	public Event(String eventType, Object[] arguments) {
		this.eventType = eventType;
		this.arguments = arguments;
	}
	
	public Event(String type) {
		this.eventType = type;
	}

	public String getEventType() {
		return eventType;
	}
	
	public Object[] getArguments() {
		return arguments;
	}

	public boolean equals(Object o) {
		if (!this.getClass().equals(o.getClass())) {
			return false;
		}
		Event other = (Event) o;
		return this.eventType.equals(other.eventType) &&
				(this.getArguments() != null &&
				this.getArguments().equals(other.getArguments())) ||
				(this.getArguments() == null && other.getArguments() == null);
	}
	
	public int hashCode() {
		return eventType.hashCode();
	}
	
	public String toString() {
		List<String> strs = new ArrayList<>();
		if (arguments != null) {
			List<Object> objs = Arrays.asList(arguments);
			strs = objs.stream().map(o -> o.toString()).collect(Collectors.toList());
		}
		
		return String.format("event(%s,%s)", eventType,String.join(",", strs));
	}
}
