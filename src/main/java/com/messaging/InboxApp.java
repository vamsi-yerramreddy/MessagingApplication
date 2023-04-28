package com.messaging;

import com.messaging.inbox.EmailFolder.EmailService;
import com.messaging.inbox.emaillist.EmailListItemRepository;
import com.messaging.inbox.folders.FolderRepository;
import com.messaging.inbox.folders.UnreadEmailStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.nio.file.Path;
import java.util.Arrays;

@SpringBootApplication
@RestController
public class InboxApp {
	@Autowired private FolderRepository folderRepository;
	@Autowired private EmailListItemRepository emailListItemRepository;
	@Autowired private UnreadEmailStatsRepository unreadEmailStatsRepository;
	@Autowired private EmailService emailService;


	public static void main(String[] args) {

		SpringApplication.run(InboxApp.class, args);
	}

    @Bean
	public CqlSessionBuilderCustomizer sessionBuilderCustomizer(DataStaxAstraProperties astraProperties){
		Path bundle = astraProperties.getSecureConnectBundle().toPath();
		return builder -> builder.withCloudSecureConnectBundle(bundle);
	}

	@PostConstruct
	public void init(){
		String currUserId="vamsi-yerramreddy";

		/*folderRepository.save(new Folder(currUserId,"Home","blue"));
		folderRepository.save(new Folder(currUserId,"Work","green"));
		folderRepository.save(new Folder(currUserId,"Bank","Yellow"));*/


		for(int i=0;i<3;i++){
			emailService.sendEmail("vamsi-yerramreddy",Arrays.asList("vamsi-yerramreddy","abc","junk"),"hello world"+i,"EmptyBody");
		}
		//List<Folder> trail = folderRepository.findAll();
		//System.out.println(trail.toString());
		//List<Folder> userFolders = folderRepository.findAllById(currUserId);
	//System.out.println("userFolders area"+ userFolders.toString());
	}

}
