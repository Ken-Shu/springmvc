package springmvc.controller;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;

import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import java.util.Map.Entry;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import springmvc.entity.User;

//母路徑 : http://localhost:8080/springmvc/mvc

//@RestController //RestController 預設就是不會自動組成jsp
@Controller
@RequestMapping("/demo")
public class DemoController {
	
	//子路徑 : /demo/hello
	@RequestMapping("/hello")
	public String hello() {
		return "hello"; //會指向 /WEB-INF/jsp/hello/jsp 頁面
	}
	//子路徑 : /demo/helloString
	@RequestMapping("/helloString")
	//如果加上ResponseBody 就會單回傳物件 而不會拼裝jsp 
	@ResponseBody
	public String helloString() {
		return "hello"; //因為有 @ResponseBody 的配置 所以直接回傳 hello 字串
	}
	
	// 帶入 name 與 age 參數
	//子路徑 : /demo/sayHi?name = John & age = 18
	@RequestMapping("/sayhi")
	@ResponseBody
	public String sayHi(@RequestParam(value = "name")String name,
						@RequestParam(value = "age") Integer age) {
		return "Hi " + name +" , " + age;
	}
	
	//帶入 name(必要參數) 與 age(非必要參數) 參數
	//子路徑 :/demo/sayhello?name=John&age=18
	//子路徑 :/demo/sayhello?name=John
	//子路徑 :/demo/sayhello? <-- 若有 defaultValue 的配置 則參數可以不用設定
	@RequestMapping("/sayhello")
	@ResponseBody
	public String sayHello(@RequestParam(value = "name", defaultValue = "unknow")String name,
			@RequestParam(value = "age" ,defaultValue = "0" ,required = false) Integer age) {
		return "Hello " + name +" , " + age;
	}
	
	//Lab :
	//子路徑 :/demo/bmi?h=170&w=60
	//輸出結果 bmi : 20.76
	//若沒有輸入參數 則顯示 :bmi :unknow
	@RequestMapping("/bmi")
	@ResponseBody
	public String bmi(@RequestParam(value = "height",defaultValue = "0")Double height,
					@RequestParam(value = "weight" ,defaultValue = "0")Double weight) {
		if(height<=0 || weight<=0) {
			return "bmi : nuknow";
		}else {
		Double bmi = weight/Math.pow(height/100, 2);
		return String.format("bmi : %.2f", bmi);
		}
	}
	
	// 路徑參數 : PathVariable
	// 子路徑 : /demo/exam/75 -> 結果 75 pass
	// 子路徑 : /demo/exam/45 -> 結果 45 fail
	@RequestMapping("/exam/{score}")
	@ResponseBody
	public String exam(@PathVariable("score") Integer score) {
		return String.format("score : %d %s", score , (score>=60)? "Pass" : "Fail");
	}
	
	/*
	 * Lab : PathVariable + RequestParam
	 * add 表示加法 sub 表示減法
	 * 子路徑 : /demo/calc/add?x=30&y=20 -> 結果 50
	 * 子路徑 : /demo/calc/sub?x=30&y=20 -> 結果 10
	 * 子路徑 : /demo/calc/abc -> 結果 無此路徑
	 * 可以使用 Optional<Integer> 來取代 Integer
	 * 配置黨要加上<mvc:annotation-driven/> 才能使用 Optional<Integer>
	 */
	@RequestMapping("/calc/{allmethod}")
	@ResponseBody
	public String getmethod(@PathVariable("allmethod") String method,
			@RequestParam(value = "x",defaultValue = "0" , required = false)Optional<Integer> x,
			@RequestParam(value = "y" ,defaultValue = "0" , required = false)Optional<Integer> y){
		
		if(x.isPresent() && y.isPresent()) {
			switch (method) {
			case "add":
				return  "add : " + (x.get() + y.get()) +"";
				
			case "sub":
				return  "sub : " + (x.get() - y.get()) +"";
				
			default:
				return "None";
			}
		}
		if(!method.equals("add") || method.equals("sub")) return "None";
		if(x.isPresent())return x.get()+"";
		if(y.isPresent())return y.get()+"";
		return "0";
	}
	// PathVariable 萬用字元 : *(任意多字) , ?(任意一字)
	// 子路徑 : /demo/any/abcdefg/java8
	// 子路徑 : /demo/any/car/java7
	// 統一印出 :　Java
	@RequestMapping("/any/*/java?")
	@ResponseBody
	public String any() {
		return "Java";
	}
	
	// RequestParam 任意多組同名參數
	// 子路徑 : /demo/age/?age=18&age=21&age=35
	// 結果 : 印出平均,最大與最小
	@RequestMapping("/age/")
	@ResponseBody
	public String age(@RequestParam("age")List<Integer> ageList) {
		IntSummaryStatistics stat = ageList.stream().mapToInt(Integer::intValue).summaryStatistics();
		return String.format("avg = %.1f , max = %d , min = %d",stat.getAverage(),stat.getMax(),stat.getMin());
	}
	// Map 參數
	// 子路徑 : /demo/score?chinese=100&english=80&math=70
	// 結果 : sum = 250
	@RequestMapping("/score")
	@ResponseBody
	public String score(@RequestParam Map<String,String> scores) {
		int sum = scores.entrySet().stream().map(Entry::getValue).mapToInt(Integer::parseInt).sum();
		return String.format("sum : %d " , sum);
	}
	
	// Java pojo(只提供屬性跟方法的物件Plan Old Java Object) 物件
	// 子路徑 : /demo/user?name=Vicent&age=18
	@RequestMapping("/user")
	@ResponseBody
	public String addUser(@Valid User user ,BindingResult result) {
		if(result.hasErrors()) {
			return String.format("add User %s Error", result);
		}
		return String.format("Add OK : %s", user);
	}
}
