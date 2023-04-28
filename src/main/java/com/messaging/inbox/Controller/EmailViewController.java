package com.messaging.inbox.Controller;

import com.messaging.inbox.emaillist.EmailListItemKey;
import com.messaging.inbox.folders.FolderRepository;
import com.messaging.inbox.EmailFolder.Email;
import com.messaging.inbox.EmailFolder.EmailRepository;
import com.messaging.inbox.EmailFolder.EmailService;
import com.messaging.inbox.emaillist.EmailListItem;
import com.messaging.inbox.emaillist.EmailListItemRepository;
import com.messaging.inbox.folders.Folder;
import com.messaging.inbox.folders.FolderService;
import com.messaging.inbox.folders.UnreadEmailStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Controller
public class EmailViewController {

    @Autowired private FolderRepository folderRepository;
    @Autowired private FolderService folderService;
    @Autowired private EmailRepository emailRepository;
    @Autowired private EmailListItemRepository emailListItemRepository;
    @Autowired private EmailService emailService;
    @Autowired private UnreadEmailStatsRepository unreadEmailStatsRepository;


    @GetMapping(value="/emails/{id}")
    public String emailView(@PathVariable UUID id,
                            @RequestParam String folder,
                            @AuthenticationPrincipal OAuth2User principal, Model model )
    {
        if( principal==null || !StringUtils.hasText(principal.getAttribute("login"))){
            return "index";
        }
        //Fetch the folders

        String userId = principal.getAttribute("login");
        List<Folder> userFolders = folderRepository.findAllById(userId);
        model.addAttribute("userFolders",userFolders);
        List<Folder> defaultFolders = folderService.fetchDefaultFolders(userId);
        model.addAttribute("defaultFolders",defaultFolders);
        model.addAttribute("userName",principal.getAttribute("name")); // showing userName



        Optional<Email> optionalEmail = emailRepository.findById(id);
        ///System.out.println("value found "+ optionalEmail);


        if(!optionalEmail.isPresent()){
            return "inbox-page";

        }

        Email email = optionalEmail.get();
        String ToIds= String.join(",",email.getTo());
        //check if the user has access to mail
        if ( ! emailService.doesHaveAccessEmail(email, userId) ) {
            return "redirect:/";

        }

        model.addAttribute("toIds",ToIds);
        model.addAttribute("email",optionalEmail.get());
        EmailListItemKey emailListItemKey = new EmailListItemKey();
        emailListItemKey.setId(userId);
        emailListItemKey.setLabel(folder);
        emailListItemKey.setTimeUUID(email.getId());

         Optional<EmailListItem> optionalEmailListItem= emailListItemRepository.findById(emailListItemKey);
         if(optionalEmailListItem.isPresent()){
             EmailListItem emailListItem =optionalEmailListItem.get();
             if(emailListItem.isUnread()){
                 emailListItem.setUnread(false);
                 emailListItemRepository.save(emailListItem);
                 unreadEmailStatsRepository.decrementUnreadCounter(userId,folder);
             }
         }
        model.addAttribute("stats", folderService.mapCountToLabels(userId));


        return "email-page";

    }

}