package com.messaging.inbox.Controller;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.messaging.inbox.emaillist.EmailListItem;
import com.messaging.inbox.emaillist.EmailListItemRepository;
import com.messaging.inbox.folders.Folder;
import com.messaging.inbox.folders.FolderRepository;
import com.messaging.inbox.folders.FolderService;
import com.messaging.inbox.folders.UnreadEmailStatsRepository;
import com.messaging.inbox.folders.*;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
public class InboxController {
    @Autowired private FolderRepository folderRepository;
    @Autowired private FolderService folderService ;
    @Autowired private EmailListItemRepository emailListItemRepository;
    @Autowired private UnreadEmailStatsRepository unreadEmailStatsRepository;

    @GetMapping(value="/")
    public String homePage(
            @RequestParam(required = false) String folder,
            @AuthenticationPrincipal OAuth2User principal,
            Model model )
       {
        if(principal==null || !StringUtils.hasText(principal.getAttribute("login"))){
            return "index";
        }
        //Fetch the folders
        String userId = principal.getAttribute("login");
        List<Folder> userFolders = folderRepository.findAllById(userId);
        model.addAttribute("userFolders",userFolders);
        List<Folder> defaultFolders = folderService.fetchDefaultFolders(userId);
        model.addAttribute("userName",principal.getAttribute("name")); // showing userName

        model.addAttribute("defaultFolders",defaultFolders);
        model.addAttribute("stats", folderService.mapCountToLabels(userId));


           if(!StringUtils.hasText(folder)){
               folder="Inbox";
           }
           List<EmailListItem> emailListItems = emailListItemRepository.findAllByKey_IdAndKey_Label(userId,folder);
           PrettyTime prettyTime= new PrettyTime();
           emailListItems.stream().forEach(emailItem -> {
               UUID timeUuid =   emailItem.getKey().getTimeUUID();
               Date emailDateTime= new Date(Uuids.unixTimestamp(timeUuid));
               emailItem.setAgoTimeString(prettyTime.format(emailDateTime) ) ;
           });

           model.addAttribute("emailList",emailListItems);
           model.addAttribute("folderName",folder);
           return "inbox-page";




    }
}
