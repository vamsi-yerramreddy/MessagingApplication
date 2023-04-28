package com.messaging.inbox.Controller;

import com.messaging.inbox.EmailFolder.Email;
import com.messaging.inbox.EmailFolder.EmailRepository;
import com.messaging.inbox.EmailFolder.EmailService;
import com.messaging.inbox.folders.Folder;
import com.messaging.inbox.folders.FolderRepository;
import com.messaging.inbox.folders.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class ComposeController {
    @Autowired  private FolderRepository folderRepository;
    @Autowired private FolderService folderService;
    @Autowired private EmailService emailService;
    @Autowired private EmailRepository emailRepository;




    @GetMapping(value="/compose")
     public String getCompose(
             @RequestParam(required = false) String to,
             @RequestParam(required = false) UUID id,
             @AuthenticationPrincipal OAuth2User principal,
             Model model
             ) {
        if (principal == null || !StringUtils.hasText(principal.getAttribute("login"))) {
            return "index";
        }
        String userID = principal.getAttribute("login");
        List<Folder> userFolders = folderRepository.findAllById((userID));
        model.addAttribute("userFolders", userFolders);
        List<Folder> defaultFolders = folderService.fetchDefaultFolders(userID);
        model.addAttribute("defaultFolders", defaultFolders);
        model.addAttribute("stats", folderService.mapCountToLabels(userID));
        model.addAttribute("userName", principal.getAttribute("name"));
        List<String> uniqueToIds = splitIds(to);
        model.addAttribute("toIds", String.join(", ", uniqueToIds));
        Optional<Email> optionalEmail = emailRepository.findById(id);//UUID id

         if(optionalEmail.isPresent()){
             Email email = optionalEmail.get();
             if(emailService.doesHaveAccessEmail(email,userID)){
                 model.addAttribute("subject", emailService.getReplySubject(email.getSubject()));
                 model.addAttribute("body",emailService.getReplyBody(email));
             }
    }
        return "compose-page";
    }
    private static List<String> splitIds(String to) {
        if(!StringUtils.hasText(to)){
            return new ArrayList<>();
        }
        String[] splitIds = to.split(",");
        List<String> uniqueToIds = Arrays.asList(splitIds)
                .stream()
                .map(id -> StringUtils.trimWhitespace(id))
                .filter(id -> StringUtils.hasText(id))
                .distinct()
                .collect(Collectors.toList());
        return uniqueToIds;
    }
    @PostMapping("/sendEmail")
    public ModelAndView sendEmail(
            @AuthenticationPrincipal OAuth2User principal,
            @RequestBody MultiValueMap<String,String> formData
    ){
        if(principal ==null || !StringUtils.hasText(principal.getAttribute("login"))) {
            return new ModelAndView("redirect:/");
        }

        String from = principal.getAttribute("login");
        String subject=  formData.getFirst("Subject");
        List<String> toIds= splitIds(formData.getFirst("toIds"));
        String body=formData.getFirst("Body");
        emailService.sendEmail(from,toIds,subject,body);

        return new ModelAndView("redirect:/");

    }

}
