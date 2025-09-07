package view;

import model.Mail;
import java.util.List;

public class MailView {
    public void showMenu() {
        System.out.println("\n===== 📧 Mail Management System =====");
        System.out.println("1. Store Mail");
        System.out.println("2. Delete Mail");
        System.out.println("3. Add Tag");
        System.out.println("4. Show Statistics");
        System.out.println("5. Search Mail");
        System.out.println("6. Wildcard Search");
        System.out.println("7. Exit");
        System.out.print("Enter your choice: ");
    }

    public void displayMails(List<Mail> mails) {
        if (mails.isEmpty()) {
            System.out.println("⚠️ No mails found.");
            return;
        }
        System.out.println("\n📨 Mails:");
        for (Mail mail : mails) {
            System.out.println("------------------------------------------------");
            System.out.println("Sender: " + mail.getSender());
            System.out.println("Receiver: " + mail.getReceiver());
            System.out.println("Subject: " + mail.getSubject());
            System.out.println("Content: " + mail.getContent());
            System.out.println("Tags: " + String.join(", ", mail.getTags()));
            System.out.println("Spam: " + (mail.isSpam() ? "Yes" : "No"));
        }
        System.out.println("------------------------------------------------");
    }

    public void showStats(int total, long spam) {
        System.out.println("\n📊 Mail Statistics:");
        System.out.println("Total Mails: " + total);
        System.out.println("Spam Mails: " + spam);
    }
}
