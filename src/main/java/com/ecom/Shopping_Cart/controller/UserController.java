package com.ecom.Shopping_Cart.controller;

import com.ecom.Shopping_Cart.model.*;
import com.ecom.Shopping_Cart.service.CartService;
import com.ecom.Shopping_Cart.service.CategoryService;
import com.ecom.Shopping_Cart.service.OrderService;
import com.ecom.Shopping_Cart.service.UserService;
import com.ecom.Shopping_Cart.util.CommonUtil;
import com.ecom.Shopping_Cart.util.OrderStatus;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String home(){
        return "user/home";
    }

    //xac dinh nguoi dung bang name trong navbar khi dang nhap

//    Principal là 1.Xác định người dùng hiện tại
//    2.Lấy thông tin người dùng trong controller
//    giúp bạn quản lý thông tin người dùng đã xác thực và tích hợp bảo mật vào ứng dụng của mình
    @ModelAttribute
    public void getUserDDetails(Principal p, Model model) {

        if(p!=null){
            String email = p.getName();
            UserDtls userDtls = userService.getUserByEmail(email);
            model.addAttribute("user", userDtls);
            Integer countCart=  cartService.getCountCart(userDtls.getId());
            model.addAttribute("countCart", countCart);
        }
        List<Category> categories = categoryService.getAllActiveCategory();
        model.addAttribute("categories", categories);
    }

    // Them san pham theo user vao gio hang
    @GetMapping("/addCart")
    public String addCart(@RequestParam int pid, @RequestParam int uid, HttpSession session){
        Cart saveCart = cartService.saveCart(pid, uid);

        if(ObjectUtils.isEmpty(saveCart)){
            session.setAttribute("errorMsg","Thêm sản phẩm vào giỏ hàng thất bại");
        }else{
            session.setAttribute("sucMsg","Thêm sản phẩm vào giỏ hàng thành công");
        }

        return "redirect:/product/"+pid;
    }

//    @PostMapping("/cart/add")
//    public String addCart(@RequestParam("pid") int pid,
//                          Principal p,
//                          RedirectAttributes ra) {
//        // lấy user từ Principal, không nhận uid từ client
//        UserDtls user = getLoggedInUserDetails(p);
//        if (pid <= 0) {
//            ra.addFlashAttribute("errorMsg", "Product không hợp lệ");
//            return "redirect:/";
//        }
//
//        try {
//            Cart saved = cartService.saveCart(pid, user.getId()); // giữ nguyên service
//            if (saved == null) {
//                ra.addFlashAttribute("errorMsg", "Thêm vào giỏ hàng thất bại");
//            } else {
//                ra.addFlashAttribute("sucMsg", "Đã thêm sản phẩm vào giỏ hàng");
//            }
//        } catch (EntityNotFoundException ex) {
//            ra.addFlashAttribute("errorMsg", "Không tìm thấy sản phẩm");
//        } catch (IllegalStateException ex) {
//            ra.addFlashAttribute("errorMsg", ex.getMessage()); // ví dụ: hết hàng
//        }
//
//        return "redirect:/product/" + pid;
//    }
    //Load san pham len gio hang
    @GetMapping("/cart")
    public String loadCartPage(Principal p,Model model){

        UserDtls user = getLoggedInUserDetails(p);
        List<Cart> carts = cartService.getCartByUser(user.getId());
        model.addAttribute("carts", carts);

        if(!carts.isEmpty()) {
            Double totalOrderPrice = carts.get(carts.size() - 1).getTotalOrderPrice();
            model.addAttribute("totalOrderPrice", totalOrderPrice);
        }
        return "user/cart";
    }

    private UserDtls getLoggedInUserDetails(Principal p) {
        String email = p.getName();
        return userService.getUserByEmail(email);
    }

    // update tang so luong va giam so luong san pham o trong gio hang
    @GetMapping("/cartQuantityUpdate")
    public String cartQuantityUpdate(@RequestParam String sy , @RequestParam int cid){
        cartService.updateQuantity(sy,cid);

        return "redirect:/user/cart";

    }
    //Load thong tin tien tung mat hang va tong tien tat ca tu gio hang theo user len view order
    @GetMapping("/order")
    public  String orderPage(Principal p, Model model){
        UserDtls user = getLoggedInUserDetails(p);
        List<Cart> carts = cartService.getCartByUser(user.getId());
        model.addAttribute("carts", carts);

//        if(carts.size()>0) {
        if(!carts.isEmpty()){
            Double orderPrice = carts.get(carts.size() - 1).getTotalOrderPrice();
            Double totalOrderPrice = carts.get(carts.size() - 1).getTotalOrderPrice()+35+100;
            model.addAttribute("orderPrice", orderPrice);
            model.addAttribute("totalOrderPrice", totalOrderPrice);
        }
        return "user/order";
    }


    //Thêm thông tin nguoi mua va bao gom mat hang khi order san pham xuong data ProductOrder
    @PostMapping("/save-order")
    public  String saveOder(@ModelAttribute OrderRequest request,Principal p) throws Exception {
//        System.out.println(request);
        UserDtls user = getLoggedInUserDetails(p);
        orderService.saveOrder(user.getId(),request);
        return "/user/success";
    }

    // Lấy IP client (phục vụ vnp_IpAddr)
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isBlank()) ip = request.getRemoteAddr();
        // Nếu có nhiều IP (proxy), lấy IP đầu tiên
        int comma = ip.indexOf(',');
        return (comma > 0) ? ip.substring(0, comma).trim() : ip;
    }


    //Load thong tin khach khi da order san pham can thanh toan len view my_order
    @GetMapping("/user-order")
    public String myOrder(Principal p,Model model){
        UserDtls loggingUser = getLoggedInUserDetails(p);

        List<ProductOrder> orders = orderService.getOrderByUser(loggingUser.getId());
        model.addAttribute("orders", orders);

        return "/user/my_order";
    }


//    update status trong don hang khi user bam cancel
    @GetMapping("/update-status")
    public String updateOrderStatus(@RequestParam int id, @RequestParam int st,HttpSession session) {

        OrderStatus[] values = OrderStatus.values();
        String status=null;
        for (OrderStatus orderSt : values){
            if(orderSt.getId()==st){
              status=orderSt.getName();
            }

        }
        ProductOrder updateStatus=orderService.UpdateOrderStatus(id,status);
        try {
            commonUtil.sendEmailForProductOrder(updateStatus,status);
        } catch (Exception e) {
           e.printStackTrace();
        }


        if(!ObjectUtils.isEmpty(updateStatus)){
            session.setAttribute("sucMsg","Trạng thái đơn hàng đã được cập nhật thành công");
        }else{
            session.setAttribute("errorMsg","Trạng thái đơn hàng không được cập nhật");
        }


        return "redirect:/user/user-order";
    }


    //load profile lên user/profile
    @GetMapping("/profile")
    public String Profile(){
        return "/user/profile";
    }


    //Cập nhật profile của user
    @PostMapping("/update-profile")
    public String updateUserProfile(@ModelAttribute UserDtls user, @RequestParam MultipartFile img,HttpSession session){
        UserDtls updateProfile = userService.updateUserProfile(user, img);
        if(ObjectUtils.isEmpty(updateProfile)){
            session.setAttribute("errorMsg","Cập nhật profile không thành công");

        }else{
            session.setAttribute("sucMsg","Profile đã được cập nhật thành công");
        }


        return "redirect:/user/profile";
    }

    //thay doi mat khau
    @PostMapping("/change-password")
    public String changePassword(@RequestParam String newPassword, @RequestParam String currentPassword,Principal p,HttpSession session){
        UserDtls loggedInUserDetails = getLoggedInUserDetails(p);

        boolean matches = passwordEncoder.matches(currentPassword, loggedInUserDetails.getPassword());
        if(matches){
            String encode = passwordEncoder.encode(newPassword);
            loggedInUserDetails.setPassword(encode);
            UserDtls userUpdate = userService.updateUser(loggedInUserDetails);
            if(ObjectUtils.isEmpty(userUpdate)){
                session.setAttribute("errorMsg","Mật khâu không được cập nhật || Lỗi trong server");
            }else{
                session.setAttribute("sucMsg","Mật khẩu đã được cập nhật thành công");
            }

        }else{
            session.setAttribute("errorMsg","Mật khẩu hiện tại không đúng");
        }
        return "redirect:/user/profile";
    }
}
