package com.dhanush.SoloSync.Service;

import com.dhanush.SoloSync.Dto.ExpenseDTO;
import com.dhanush.SoloSync.Model.ProfileEntity;
import com.dhanush.SoloSync.Repository.ProfileEntityRepo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class NotificationService {

    @Autowired
    ProfileEntityRepo profileEntityRepo;
    @Autowired
    EmailService emailService;
    @Autowired
    ExpenseService expenseService;

    @Value("${solosync.frontend.url}")
    String frontendUrl;

    @Scheduled(cron = "0 0 22 * * *",zone = "IST")
    public void sendDailyIncomeExpenseReminder(){
        log.info("Job started: sendDailyIncomeExpenseReminder()");
        List<ProfileEntity> profiles = profileEntityRepo.findAll();
        for(ProfileEntity profile:profiles)
        {
            String body = "Hi "+profile.getFullname() + ",<br><br>"
                    +"This is a friendly reminder to add your income and expenses for today in Money.<br><br>"
                    +"<a href="+frontendUrl+" style='display:inline-block;padding:10px 20px;background-color:#4CAF50;color:#ffff;text-decoration:none;border-radius:5px;font-weight:bold;'> Go to SoloSync</a>"
                    +"<br><br> Best Regards, <br> SOLOSYNC TEAM <br> DHANUSH SUBRAMANI S";
            emailService.sendEmail(profile.getEmail(), body,"Daily reminder: Add your income and expenses");
        }
        log.info("Job completed: SendDailyIncomeExpenseReminder()");
    }

    @Scheduled(cron = "0 0 23 * * *",zone = "IST")
    public void sendDailyIncomeExpenseReport(){
        log.info("Job started: SendDailyIncomeExpenseReport()");
        List<ProfileEntity> profiles = profileEntityRepo.findAll();
        for(ProfileEntity profile:profiles){
            List<ExpenseDTO> expenseEntities = expenseService.getExpensesForUserDate(profile.getId(), LocalDate.now());
            if(!expenseEntities.isEmpty()){
                StringBuilder table = new StringBuilder();
                table.append("<table style = 'border-collapse:collapse;width:100%'>");
                table.append("<tr style='background-color:#f2f2f2;'> <th style='border:1px solid #ddd;padding:8px;'>Amount</th> <th style='border:1px solid #ddd;padding:8px;'>Category</th> <th style='border:1px solid #ddd;padding:8px;'>Date</th> </tr>");
                int i = 1;
                for(ExpenseDTO DTO:expenseEntities){
                    table.append("<tr>");
                    table.append("<tb style='border:1px solid #ddd;padding:8px;'>").append(i++).append("</td>");
                    table.append("<tb style='border:1px solid #ddd;padding:8px;'>").append(DTO.getAmount()+"").append("</td>");
                    table.append("<tb style='border:1px solid #ddd;padding:8px;'>").append(DTO.getName()).append("</td>");
                    table.append("<tb style='border:1px solid #ddd;padding:8px;'>").append(DTO.getDate()).append("</td>");
                    table.append("</tr>");
                }
                table.append("</table>");
                String body = "Hi "+profile.getFullname()+", <br/><br/> here is a summary of your expense for today:"+ table +"<br/><br/> Best Regards, <br> SOLOSYNC TEAM <br> DHANUSH SUBRAMANI S";
                emailService.sendEmail(profile.getEmail(),body,"Daily Report: Your today expenses report");
            }
        }
        log.info("Job completed: SendDailyIncomeExpenseReport()");

    }
}
