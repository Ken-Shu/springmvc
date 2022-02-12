package springmvc.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import springmvc.entity.Employee;


@Controller()
@RequestMapping("/employee")
public class EmployeeController {
	private List<Employee> employees = new ArrayList<>();
	@GetMapping("/")
	public String index(Model model , @ModelAttribute Employee employee) {
//		employee.setName("小王");
//		employee.setAge(18);
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		try {
//			employee.setBirth(sdf.parse("2010-5-12"));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		employee.setEducation("大學");
//		employee.setSex("男");
//		employee.setInterest(new String[] {"閱讀","打球"});
//		employee.setResume("我愛寫程式...");
//		employee.setSalary(88000);
		model.addAttribute("employees",employees);
		return "employee/index";
	}
	@PostMapping
	public String add(Employee employee , RedirectAttributes attr) {
		employees.add(employee);
//		try {
//			System.out.println(new String(employee.getName().getBytes("ISO-8859-1"), "UTF-8"));
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
		attr.addFlashAttribute("employee",employee);
		return "redirect:./addOk";
	}
	@GetMapping(value = {"/addOk" , "/updateOk"})
	public String success() {
		return "employee/success";
	}
	
	@GetMapping
	public String get(@PathVariable("index") int index , Model model) {
		Employee emp = employees.get(index);
		model.addAttribute("employee" ,emp);
		model.addAttribute("index",index); //要多加入index 給修改頁面
		return "employee/update";
	}
	@PutMapping(value = "/{index}")
	public String update(@PathVariable("index") int index , Employee employee , RedirectAttributes attr) {
		employees.set(index, employee);
		attr.addFlashAttribute("employee" , employee);
		return "redirect:./updateOk";
	}
	
	@GetMapping(value = "/{index}/salary")
	public String update(@PathVariable("index") int index ,@RequestParam("salary")int salary) {
		Employee emp = employees.get(index);
		emp.setSalary(salary);
		return "redirect:../";
	}
	
	@DeleteMapping(value = "/{index}")
	public String delete(@PathVariable("index") int index) {
		employees.remove(index);		
		return "redirect:./";
	}
}
