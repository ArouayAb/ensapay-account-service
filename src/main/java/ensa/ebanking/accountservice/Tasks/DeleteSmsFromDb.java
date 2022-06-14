package ensa.ebanking.accountservice.Tasks;



import ensa.ebanking.accountservice.DAO.SmsDao;
import ensa.ebanking.accountservice.Entities.Sms;


import java.util.TimerTask;

public class DeleteSmsFromDb extends TimerTask {
    private Sms smsToDelete;
    private SmsDao smsDao;
    public DeleteSmsFromDb(Sms smsToDelete, SmsDao smsDao){
        this.smsToDelete=smsToDelete;
        this.smsDao=smsDao;
    }
    @Override
    public void run() {
      smsDao.delete(smsToDelete);
    }
}
