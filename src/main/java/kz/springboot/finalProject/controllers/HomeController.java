package kz.springboot.finalProject.controllers;

import kz.springboot.finalProject.entities.*;
import kz.springboot.finalProject.services.ItemService;
import kz.springboot.finalProject.services.RequestService;
import kz.springboot.finalProject.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Autowired
    private RequestService requestService;

    @GetMapping(value="/")
    public String index(Model model){
        if(getUserData()!=null){
            model.addAttribute("currentUser", getUserData())  ;
        }
        List<ShopItems> items = itemService.getAllItems();
        model.addAttribute("tovary", items);
        List<Countries> countries = itemService.getAllCountries();
        model.addAttribute("countries", countries);
        return "index";
    }

    @GetMapping(value="/managingUsers")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public String managingUsers(Model model){
        model.addAttribute("currentUser", getUserData())  ;
        List<Users> users = userService.getAllUsers();
        model.addAttribute("users", users);

        return "managingUsers";
    }

    @GetMapping(value="/managingRequests")
    @PreAuthorize("hasAnyRole('ROLE_MODERATOR', 'ROLE_ADMIN')")
    public String managingRequests(Model model){
        model.addAttribute("currentUser", getUserData())  ;
        List<Requests> requests = requestService.getAllRequests();
        model.addAttribute("requests", requests);

        return "managingRequests";
    }

    @GetMapping(value="/about")
    public String about(Model model){

        model.addAttribute("currentUser", getUserData());
        return "about";
    }

    @PostMapping(value = "/additem")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String addItem(@RequestParam(name="country_id", defaultValue = "0") Long id,
                          @RequestParam(name="item_name", defaultValue = "No Item") String name,
                          @RequestParam(name="item_price", defaultValue = "0") int price,
                          @RequestParam(name="item_amount", defaultValue = "0") int amount){
        Countries cnt = itemService.getCountry(id);
        if (cnt != null) {

            ShopItems item = new ShopItems();
            item.setName(name);
            item.setPrice(price);
            item.setAmount(amount);
            item.setCountry(cnt);
            itemService.addItem(item);

        }
        return "redirect:/";
    }

    @GetMapping(value = "/details/{idshka}")
    public String details(Model model, @PathVariable(name="idshka") Long id){
        model.addAttribute("currentUser", getUserData())  ;
        ShopItems item = itemService.getItem(id);
        model.addAttribute("item", item);

        List<Requests> requests = requestService.getRequestByUserId(getUserData().getId());
        for(int i=0; i<requests.size(); i++){
            if(id==requests.get(i).getShopItems().getId()){
                model.addAttribute("hasTour", requests);
            }
        }


        List<Countries> countries = itemService.getAllCountries();
        model.addAttribute("countries", countries);

        return "details";
    }



    @GetMapping(value = "/edititem/{idshka}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String edititem(Model model, @PathVariable(name="idshka") Long id){
        model.addAttribute("currentUser", getUserData())  ;
        ShopItems item = itemService.getItem(id);
        model.addAttribute("item", item);

        List<Countries> countries = itemService.getAllCountries();
        model.addAttribute("countries", countries);

        return "edititem";
    }


    @PostMapping(value = "/saveitem")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String saveItem(@RequestParam(name="id", defaultValue = "0") Long id,
                           @RequestParam(name="item_name", defaultValue = "No Item") String name,
                          @RequestParam(name="item_price", defaultValue = "0") int price,
                          @RequestParam(name="item_amount", defaultValue = "0") int amount,
                           @RequestParam(name="country_id", defaultValue = "0") Long country_id){
        ShopItems item = itemService.getItem(id);
        Countries cnt = itemService.getCountry(country_id);
        if(item!=null){
            item.setName(name);
            item.setPrice(price);
            item.setAmount(amount);
            item.setCountry(cnt);
            itemService.saveItem(item);
        }


        return "redirect:/edititem/" + id;
    }

    @PostMapping(value = "/deleteItem")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String deleteItem(@RequestParam(name="id", defaultValue = "0") Long id){
        ShopItems item = itemService.getItem(id);
        if(item!=null){

            List<Requests> requests = requestService.getRequestsByShopItem(item);
            for(int i = 0; i<requests.size(); i++){
                requestService.deleteRequest(requests.get(i));
            }

            itemService.deleteItem(item);
        }

        return "redirect:/";
    }

    @GetMapping(value="/403")
    public String accessDenied(Model model){

        model.addAttribute("currentUser", getUserData())  ;
        return "403";
    }

    @GetMapping(value="/login")
    public String login(Model model){
        model.addAttribute("currentUser", getUserData())  ;
        return "login";
    }


    @GetMapping(value="/profile")
    @PreAuthorize("isAuthenticated()")
    public String profile(Model model){
        model.addAttribute("currentUser", getUserData())  ;
        List<Requests> requests = requestService.getRequestByUserId(getUserData().getId());

        if(requests!=null){
            model.addAttribute("hasTour", requests);
        }
        return "profile";
    }

    @PostMapping(value = "/deleteUser")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR', 'ROLE_USER')")
    public String deleteUser(@RequestParam(name="id", defaultValue = "0") Long id){
        Users user = userService.getUserById(id);
        if(user!=null){
            List<Requests> requests = requestService.getRequestByUserId(user.getId());
            for(int i = 0; i<requests.size(); i++){
                requestService.deleteRequest(requests.get(i));
            }
            userService.deleteUser(user);

        }
        return "redirect:/";
    }

    @PostMapping(value = "/saveUser")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR', 'ROLE_USER')")
    public String saveUser(@RequestParam(name="id", defaultValue = "0") Long id,
                           @RequestParam(name="user_name", defaultValue = "") String name,
                           @RequestParam(name="user_email", defaultValue = "") String email,
                           @RequestParam(name="user_password", defaultValue = "") String password){
        Users user = userService.getUserById(id);
        if(user!=null){
            user.setFullName(name);
            user.setEmail(email);
            user.setPassword(password);
            userService.saveUser(user);
        }

        return "redirect:/profile";
    }



    @GetMapping(value="/additem")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public String addItem(Model model){
        model.addAttribute("currentUser", getUserData())  ;

        List<Countries> countries = itemService.getAllCountries();
        model.addAttribute("countries", countries);

        return "additem";
    }



    @PostMapping(value = "/addRequest")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR', 'ROLE_USER')")
    public String addRequest(@RequestParam(name="item_id", defaultValue = "0") Long item_id,
                          @RequestParam(name="id_client", defaultValue = "0") Long id_client,
                          @RequestParam(name="date", defaultValue = "") String date,
                          @RequestParam(name="ph_number", defaultValue = "") String ph_number){
        ShopItems cnt = itemService.getItem(item_id);
        Users user = userService.getUserById(id_client);

        if (cnt != null) {
            Requests item = new Requests();
            item.setDate(date);
            item.setPhNumber(ph_number);
            item.setUser(user);
            item.setShopItems(cnt);
            requestService.addRequest(item);
        }
        return "redirect:/profile";
    }



    @GetMapping(value="/register")
    public String register(Model model){

        model.addAttribute("currentUser", getUserData());
        return "register";
    }

    @PostMapping(value = "/register")
    public String toRegister(@RequestParam(name="user_email") String email,
                           @RequestParam(name="user_password") String password,
                           @RequestParam(name="re_user_password") String rePassword,
                           @RequestParam(name="user_full_name") String fullName){
        if (password.equals(rePassword)) {

            Users newUser = new Users();
            newUser.setFullName(fullName);
            newUser.setPassword(password);
            newUser.setEmail(email);
            if (userService.createUser(newUser)!=null){
                return "redirect:/register?success";
            }
        }


        return "redirect:/register?error";
    }

//=---------------------------------------------------------------------------------

    @GetMapping(value="/profileUsers/{idshka}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public String profileUsers(Model model, @PathVariable(name="idshka") Long id){
        model.addAttribute("currentUser", getUserData())  ;
        Users users = userService.getUserById(id);
        model.addAttribute("user", users);

        return "profileUsers";
    }


    @PostMapping(value = "/deleteProfileUser")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public String deleteProfileUser(@RequestParam(name="id", defaultValue = "0") Long id){
        Users user = userService.getUserById(id);
        if(user!=null){
            userService.deleteUser(user);
        }
        return "redirect:/";
    }

    @GetMapping(value = "/editUserManaging/{idshka}")
    @PreAuthorize("hasAnyRole('ROLE_MODERATOR')")
    public String editUserManaging(Model model, @PathVariable(name="idshka") Long id){
        model.addAttribute("currentUser", getUserData())  ;
        Users user = userService.getUserById(id);
        model.addAttribute("user", user);

        return "managingUsers";
    }

    @PostMapping(value = "/saveUserManaging")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public String saveUserManaging(@RequestParam(name="id", defaultValue = "0") Long id,
                           @RequestParam(name="user_name", defaultValue = "") String name,
                           @RequestParam(name="user_email", defaultValue = "") String email,
                           @RequestParam(name="user_password", defaultValue = "") String password){
        Users user = userService.getUserById(id);
        if(user!=null){
            user.setFullName(name);
            user.setEmail(email);
            user.setPassword(password);
            userService.saveUser(user);
        }

        return "redirect:/managingUsers";
    }




    private Users getUserData(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            User secUser = (User)authentication.getPrincipal();
            Users myUser =userService.getUserByEmail(secUser.getUsername());
            return myUser;
        }
        return null;
    }
}
