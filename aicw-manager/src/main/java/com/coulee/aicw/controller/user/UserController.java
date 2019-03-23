package com.coulee.aicw.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.coulee.aicw.entity.UserEntity;
import com.coulee.aicw.foundations.controller.BaseController;
import com.coulee.aicw.foundations.entity.Message;
import com.coulee.aicw.foundations.entity.PageEntity;
import com.coulee.aicw.foundations.utils.page.PageArg;
import com.coulee.aicw.foundations.utils.page.PageList;
import com.coulee.aicw.service.user.IUserService;

import oracle.jdbc.proxy.annotation.Post;

/***
 * 人员管理
 * @author tongjie
 *
 */
@RestController
@RequestMapping("/user")
public class UserController extends BaseController{

	@Autowired
	private IUserService userService;
	
	@RequestMapping("/list")
	public PageEntity<UserEntity> list(@RequestBody UserEntity entity) {
		PageArg pageArg = this.getPageArg(entity);
		PageList<UserEntity> pl = this.userService.findByEntity(entity, pageArg);
		return this.makePageEntity(pl);
	}
	
	@RequestMapping("/add")
	public Message addUser(@RequestBody UserEntity entity) {
		return userService.add(entity);
	}
	
	@RequestMapping("/delete")
	public Message deleteUser(@RequestBody UserEntity entity) {
		return userService.delete(entity.getUserId());
	}
	
	@RequestMapping("/update")
	public Message updateUser(@RequestBody UserEntity entity) {
		return userService.updateUser(entity);
	}
	@PostMapping("/isExistAccount")
	public Message isExistAccount(@RequestBody UserEntity user) {
		Boolean exist = userService.isExistAccount(user.getUserAccount());
		return Message.newSuccessMessage(exist.toString());
	}
	
}
