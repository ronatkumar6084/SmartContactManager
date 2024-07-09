package com.scm.controllers;

import java.security.Principal;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.forms.ContactForm;
import com.scm.forms.ContactSearchForm;
import com.scm.helper.AppConstants;
import com.scm.helper.Message;
import com.scm.helper.MessageType;
import com.scm.services.ContactService;
import com.scm.services.ImageService;
import com.scm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/user/contacts")
public class ContactController {

    private Logger logger = LoggerFactory.getLogger(ContactController.class);

    @Autowired
    private ImageService imageService;

    @Autowired
    private ContactService contactService;

    @Autowired
    private UserService userService;

    //add contact page: handle
    @RequestMapping("/add")
    public String addContactView(Model model)
    {
        ContactForm contactForm = new ContactForm();
        model.addAttribute("contactForm", contactForm);
        return "user/add_contact";
    }

    @RequestMapping(value="/add",method = RequestMethod.POST)
    public String saveContact(@Valid @ModelAttribute ContactForm contactForm,Principal principal, BindingResult bindingResult,HttpSession httpSession)
    {
        //validate form validation logic
        if(bindingResult.hasErrors()){
            httpSession.setAttribute("message", Message.builder()
            .content("Please correct the following errors")
            .type(MessageType.red)
            .build());
            return "user/add_contact" ;
        }

        //process the form data
        String username = principal.getName();
        //form ---> convert ----> contact
        User user = userService.getUserByEmail(username);
        Contact contact = new Contact();
        contact.setName(contactForm.getName());
        contact.setEmail(contactForm.getEmail());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        contact.setAddress(contactForm.getAddress());
        contact.setFavorite(contactForm.isFavorite());
        contact.setDescription(contactForm.getDescription());
        contact.setUser(user);
        contact.setLinkedInLink(contactForm.getLinkedInLink());
        contact.setWebsiteLink(contactForm.getWebsiteLink());

         //process picture
        //image upload code
        if(contactForm.getContactImage() != null && !contactForm.getContactImage().isEmpty()){
            String filename = UUID.randomUUID().toString();
            String fileURL = imageService.uploadImage(contactForm.getContactImage(),filename);
            contact.setPicture(fileURL);
            contact.setCloudinaryPublicImageId(filename);
        }

        //saving contact
        contactService.saveContact(contact);
        System.out.println(contactForm);

        //ast message to display
        httpSession.setAttribute("message", Message.builder()
        .content( "You have successfully added a new contact")
        .type(MessageType.green)
        .build());
        return "redirect:/user/contacts/add";
    }

    //view contacts
    @RequestMapping
    public String viewContacts(
    @RequestParam(value="page",defaultValue="0") int page,
    @RequestParam(value="size",defaultValue= AppConstants.PAGE_SIZE+ "") int size,
    @RequestParam(value="sortBy",defaultValue="name") String sortBy,
    @RequestParam(value="direction",defaultValue="asc") String direction, Principal principal, Model model)
    {
        //load all the user contacts
        String username= principal.getName();
        User user = userService.getUserByEmail(username);
        Page <Contact> pageContacts =  contactService.getByUser(user, page, size, sortBy, direction);
        model.addAttribute("pageContacts", pageContacts);
        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);
        model.addAttribute("contactSearchForm", new ContactSearchForm());
        return "/user/contacts";
    }

    //search contact handler
    @RequestMapping("/search")
    public String search(
        @ModelAttribute ContactSearchForm contactSearchForm,
        @RequestParam(value="size", defaultValue = AppConstants.PAGE_SIZE+"") int size,
        @RequestParam(value="page",defaultValue = "0") int page,
        @RequestParam(value="sortBy",defaultValue="name") String sortBy,
        @RequestParam(value="direction",defaultValue="asc") String direction, Model model,Principal principal)
    {
        logger.info("field {} keyword {}", contactSearchForm.getField(),contactSearchForm.getValue());
        
        String username = principal.getName();
        User user = userService.getUserByEmail(username);

        Page <Contact> pageContact = null;
        if(contactSearchForm.getField().equalsIgnoreCase("name")){
             pageContact = contactService.searchByName(contactSearchForm.getValue(),size,page,sortBy,direction,user);
        }else if(contactSearchForm.getField().equalsIgnoreCase("email")){
            pageContact = contactService.searchByEmail(contactSearchForm.getValue(),size,page,sortBy,direction,user);
        }else if(contactSearchForm.getField().equalsIgnoreCase("phoneNumber")){
            pageContact = contactService.searchByPhoneNumber(contactSearchForm.getValue(),size,page,sortBy,direction,user);
        }
        logger.info("pageContact {}", pageContact);
        model.addAttribute("pageContact", pageContact);
        model.addAttribute("contactSearchForm",contactSearchForm);
        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);
        return "user/search";
    }

    //delete contact
    @RequestMapping("/delete/{contactId}")
    public String deleteContact(@PathVariable("contactId") String contactId, HttpSession httpSession){
        contactService.deleteContact(contactId);
        logger.info("contactId {} deleted",contactId);
                   httpSession.setAttribute("message", 
                   Message.builder()
                   .content("Contact is deleted successfully.")
                   .type(MessageType.green)
                   .build());
        return "redirect:/user/contacts";
    }

    //update contact from view
    @GetMapping("/view/{contactId}")
    public String updateContactFromView(@PathVariable ("contactId") String contactId,Model model)
    {
        var contact = contactService.getContactById(contactId);
        ContactForm contactForm = new ContactForm();
        contactForm.setName(contact.getName());
        contactForm.setEmail(contact.getEmail());
        contactForm.setPhoneNumber(contact.getPhoneNumber());
        contactForm.setAddress(contact.getAddress());
        contactForm.setDescription(contact.getDescription());
        contactForm.setFavorite(contact.isFavorite());
        contactForm.setWebsiteLink(contact.getWebsiteLink());
        contactForm.setLinkedInLink(contact.getLinkedInLink());
        contactForm.setPicture(contact.getPicture());

        model.addAttribute("contactForm", contactForm);
        model.addAttribute("contactId", contactId);
        return "user/update_contact_view";    
    }

    @PostMapping("/update/{contactId}")
    public String updateContact(@Valid @PathVariable("contactId")String contactId, @ModelAttribute ContactForm contactForm,Model model,BindingResult bindingResult,HttpSession httpSession)
    {
        if(bindingResult.hasErrors()){
            return "user/update_contact_view";
        }
        //update contact
        //var con = new Contact();
        var con = contactService.getContactById(contactId);
        con.setId(contactId);
        con.setName(contactForm.getName());
        con.setEmail(contactForm.getEmail());
        con.setPhoneNumber(contactForm.getPhoneNumber());
        con.setAddress(contactForm.getAddress());
        con.setDescription(contactForm.getDescription());
        con.setFavorite(contactForm.isFavorite());
        con.setWebsiteLink(contactForm.getWebsiteLink());
        con.setLinkedInLink(contactForm.getLinkedInLink());
        
        //process image
        if(contactForm.getContactImage() != null && !contactForm.getContactImage().isEmpty()){
           logger.info("file is not empty.");
            String fileName = UUID.randomUUID().toString();
            String imageUrl =  imageService.uploadImage(contactForm.getContactImage(),fileName);
            con.setCloudinaryPublicImageId(fileName);
            con.setPicture(imageUrl);
            contactForm.setPicture(imageUrl);
        }else{
            logger.info("file is empty");
        }

        var updatedCon = contactService.updateContact(con);
        logger.info("updated contact {}", updatedCon);
               httpSession.setAttribute("message", 
               Message.builder()
              .content("Contact Updated")
              .type(MessageType.green)
              .build());
        return "redirect:/user/contacts/view/" + contactId;
    }
    
}
