package com.ecom.Shopping_Cart.controller;

import com.ecom.Shopping_Cart.model.Category;
import com.ecom.Shopping_Cart.model.Product;
import com.ecom.Shopping_Cart.model.UserDtls;
import com.ecom.Shopping_Cart.service.*;
import com.ecom.Shopping_Cart.util.CommonUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Controller
public class HomeController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private Emailservice emailservice;

    //xac dinh nguoi dung va hien thi name trong navbar khi dang nhap
    @ModelAttribute
    public void getUserDDetails(Principal p, Model model) {

        if (p != null) {
            String email = p.getName();
            UserDtls userDtls = userService.getUserByEmail(email);
            model.addAttribute("user", userDtls);
            Integer countCart = cartService.getCountCart(userDtls.getId());
            model.addAttribute("countCart", countCart);
        }

        //dung de load category trên phan navbar
        List<Category> categories = categoryService.getAllActiveCategory();
        model.addAttribute("categories", categories);

    }
    @GetMapping("/")
    public String index(Model model) {
        List<Category> categories = categoryService.getAllActiveCategory().stream().limit(6).toList();
        List<Product> products = productService.getAllIsActiveProduct("").stream().limit(8).toList();

        model.addAttribute("categories", categories);
        model.addAttribute("products", products);

        return "index";
    }

    @GetMapping("/signin")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }


    //Load product on the product and pagination
    @GetMapping("/products")
    public String products(Model model, @RequestParam(value = "category", defaultValue = "") String category,
                           @RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
                           @RequestParam(value = "pageSize", defaultValue = "4") Integer pageSize, @RequestParam(defaultValue = "") String ch) {
//        System.out.println("Category: " + category);
        List<Category> categories = categoryService.getAllActiveCategory();
        model.addAttribute("categories", categories);
        model.addAttribute("paramValue", category);


//        List<Product> products =productService.getAllIsActiveProduct(category);
//        model.addAttribute("products",products);
        Page<Product> page = null;

        if (StringUtils.isEmpty(ch)) {
            page = productService.getAllActiveProductPagination(pageNo, pageSize, category);

        } else {
            page = productService.searchActiveProductPagination(pageNo, pageSize, category, ch);
        }

        List<Product> products = page.getContent();//lấy nội dung sản phẩm

        model.addAttribute("products", products);
        model.addAttribute("productsSize", products.size());
        model.addAttribute("pageNo", page.getNumber());
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalElements", page.getTotalElements());
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("isFirst", page.isFirst());
        model.addAttribute("isLast", page.isLast());


        return "product";
    }


    //Load thong tin trong detail view_product
    @GetMapping("/product/{id}")
    public String viewProduct(@PathVariable("id") int id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "view_product";
    }

    //Update thong tin dang ki
    @PostMapping("/saveRegister")
    public String saveRegister(@ModelAttribute UserDtls userDtls, @RequestParam("img") MultipartFile file, HttpSession session) throws IOException {

        Boolean existsEmail = userService.existsEmail(userDtls.getEmail());
        if (existsEmail) {
            session.setAttribute("errorMsg", "Email đã tồn tại");
        } else {


            String imageName = file.isEmpty() ? "default.jpg" : file.getOriginalFilename();
            userDtls.setProfileImage(imageName);
            UserDtls saveUserDtls = userService.saveUser(userDtls);


            if (!ObjectUtils.isEmpty(saveUserDtls)) {
                if (!file.isEmpty()) {
                    //Luu vao duong dan hinh anh trong O D:/ sau khi nhan luu thong tin
                    File saveFile = new ClassPathResource("static/img").getFile();

                    Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "profile_img" + File.separator + file.getOriginalFilename());

//                System.out.println(path);

                    Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                }
                session.setAttribute("sucMsg", "Đăng ký thành công, vui lòng đăng nhập");
            } else {
                session.setAttribute("errorMsg", "Đăng ký không thành công, vui lòng thử lại");
            }
        }

        return "redirect:/register";
    }

    @GetMapping("/search")
    public String searchProduct(@RequestParam String ch, Model model) {

        List<Product> searchProduct = productService.searchProduct(ch);
        model.addAttribute("products", searchProduct);

        return "product";
    }

    //quen mat khau
    @GetMapping("/forgot-password")
    public String showForgotPass() {
        return "forgot_password";
    }


//    //Quen mat khau va gui email user
//    @PostMapping("/forgot-password")
//    public String processForgotPassWord(@RequestParam String email, HttpSession session, HttpServletRequest request){
//        UserDtls userByEmail = userService.getUserByEmail(email);
//        if(ObjectUtils.isEmpty(userByEmail)){
//            session.setAttribute("errorMsg", "Email không tồn tại trong hệ thống");
//        }else {
//            String resetToken = UUID.randomUUID().toString();
//            userService.updateUserResetToken(email, resetToken);
//
//            // Generate URL :
//            // http://localhost:8080/reset-password?token=sfgdbgfswegfbdgfewgvsrg
//            String url = CommonUtil.generateUrl(request);
//            Boolean send = commonUtil.sendMail();
//              if (send){
//                  session.setAttribute("sucMsg", "Vui lòng kiểm tra email của bạn để đặt lại mật khẩu");
//              }else{
//                  session.setAttribute("errorMsg", "Không thể gửi email, vui lòng thử lại sau");
//              }
//        }
//        return "redirect:/forgot-password";

    // }
    @PostMapping("/forgot-password")
    public String processForgotPassWord(@RequestParam String email,
                                        RedirectAttributes ra,
                                        HttpServletRequest request) {
        UserDtls user = userService.getUserByEmail(email);
        // (Khuyến nghị: luôn trả về success để tránh lộ tồn tại email)
        if (user == null) {
            ra.addFlashAttribute("sucMsg", "Nếu email tồn tại, chúng tôi đã gửi liên kết đặt lại.");
            return "redirect:/forgot-password";
        }

        String token = UUID.randomUUID().toString();
        userService.updateUserResetToken(email, token); // nhớ lưu thêm expired_at

        // http://host:port/reset-password?token=...
        String base = request.getRequestURL().toString().replace(request.getRequestURI(), "");
        String resetLink = UriComponentsBuilder.fromHttpUrl(base)
                .path("/reset-password")
                .queryParam("token", token)
                .build()
                .toString();


        boolean sent = emailservice.sendPasswordReset(email, resetLink);
        if (sent) {
            ra.addFlashAttribute("sucMsg", "Vui lòng kiểm tra email để đặt lại mật khẩu.");
        } else {
            ra.addFlashAttribute("errorMsg", "Không thể gửi email, vui lòng thử lại sau.");
        }
        return "redirect:/forgot-password";
    }

    @GetMapping("/reset-password")
    public String showResetPass(@RequestParam String token, Model model) {
        model.addAttribute("token", token);
        return "reset_password";
    }

    @PostMapping("/reset-password")
    public String handleReset(@RequestParam String token,
                              @RequestParam String password,
                              @RequestParam String confirmPassword,
                              RedirectAttributes ra) {
        // 1) validate input
        if (password == null || password.length() < 6) {
            ra.addFlashAttribute("errorMsg", "Mật khẩu phải từ 6 ký tự.");
            return "redirect:/reset-password?token=" + token;
        }
        if (!password.equals(confirmPassword)) {
            ra.addFlashAttribute("errorMsg", "Mật khẩu nhập lại không khớp.");
            return "redirect:/reset-password?token=" + token;
        }
        // 2) gọi service đổi mật khẩu theo token
        boolean ok = userService.resetPasswordByToken(token, password);
        if (!ok) {
            ra.addFlashAttribute("errorMsg", "Liên kết không hợp lệ hoặc đã hết hạn.");
            return "redirect:/reset-password?token=" + token;
        }
        // 3) xong thì về trang đăng nhập
        ra.addFlashAttribute("sucMsg", "Đổi mật khẩu thành công. Vui lòng đăng nhập.");
        return "redirect:/signin";
    }
}
