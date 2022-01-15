package springmvc.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import springmvc.entity.User;

@Service
public class UserService {
	private static List<User> users = new ArrayList<>();
	
	public boolean create(User user) {
		Optional<User> optuser = getByName(user.getName());
		if(!optuser.isPresent()) {
			users.add(user);
		}
		return !optuser.isPresent();
	}
	public List<User> read() {
		return users;
	}
	public Optional<User> getByName(String name) {
		Optional<User> optuser = users.stream().filter(e -> e.getName().equals(name)).findFirst();
		return optuser;
	}
	public boolean updateAgeByName(String name , Integer newAge) {
		Optional<User> optuser = getByName(name);
		if(optuser.isPresent()) {
			User user = optuser.get();
			user.setAge(newAge);
		}
		return optuser.isPresent();
	}
	public boolean deleteByName(String name) {
		Optional<User> optuser = getByName(name);
		if(optuser.isPresent()) {
			User user = optuser.get();
			users.remove(user);
		}
		return optuser.isPresent();
	}
}
