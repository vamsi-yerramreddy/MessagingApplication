package io.javabrains.inbox.EmailFolder;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import io.javabrains.inbox.emaillist.EmailListItem;
import io.javabrains.inbox.emaillist.EmailListItemKey;
import io.javabrains.inbox.emaillist.EmailListItemRepository;
import io.javabrains.inbox.folders.Folder;
import io.javabrains.inbox.folders.UnreadEmailStats;
import io.javabrains.inbox.folders.UnreadEmailStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {

    @Autowired private EmailRepository emailRepository;
    @Autowired private UnreadEmailStatsRepository unreadEmailStatsRepository;
    @Autowired private EmailListItemRepository emailListItemRepository;


    public void sendEmail(String from, List<String> to, String subject, String body) {

        // Create a  mail
        Email email = new Email();
        email.setFrom(from);

        email.setTo(to);
        email.setSubject(subject);
        email.setBody(body);
        email.setId(Uuids.timeBased());
        emailRepository.save(email);


        //create a list item in toId's Inbox and increment the counter.

        to.forEach(toId ->{
            EmailListItem item = createEmailListItem(to, subject, email, toId,"Inbox");
            emailListItemRepository.save(item);
            unreadEmailStatsRepository.incrementUnreadCounter(toId,"Inbox");
        });

       EmailListItem sentItemsEntry= createEmailListItem(to,subject,email,from,"sentItems");
       sentItemsEntry.setUnread(false);
       emailListItemRepository.save(sentItemsEntry);

    }
    //check if the user has access to Email

    public boolean doesHaveAccessEmail(Email email, String userId){
        return( userId.equals(email.getFrom()) || email.getTo().contains(userId));
    }
    public String getReplySubject(String subject){
        return "Re: "+subject;
    }
    public String getReplyBody(Email email){
        return "\n\n\n ------------------------------------\n" +
                "From : " + email.getFrom() + "\n"+
                "To : " + email.getTo() + "\n\n" +
                email.getBody();
    }
    private static EmailListItem createEmailListItem(List<String> to, String subject, Email email, String itemOwner, String folder) {
        EmailListItemKey key = new EmailListItemKey();
        key.setId(itemOwner);
        key.setLabel(folder);
        key.setTimeUUID(email.getId());

        EmailListItem item = new EmailListItem();
        item.setKey(key);
        item.setTo(to);
        item.setSubject(subject);
        item.setUnread(true);

        return item;
    }
}
