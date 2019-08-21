package core.Game;

import core.Persisit.sqlCommand;
import core.DataModel.userInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Credit {
    private static Logger logger = LogManager.getLogger(Credit.class);

    public static void appendUserCredit(String phoneNumber, int creditValue) {
        userInfo employee;
        Transaction transaction = null;
        try (Session session = sqlCommand.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            int readCreditVal = 0;
            String hql = "FROM userInfo E WHERE E.phoneNumber = :phoneNumber";
            Query query = session.createQuery(hql);
            AtomicReference<String> getUserPhone = new AtomicReference<>("");
            query.setParameter("phoneNumber", phoneNumber);
            List qq = query.list();
            for (Iterator iterator1 = qq.iterator(); iterator1.hasNext(); ) {
                employee = (userInfo) iterator1.next();
                logger.info(" Lookup phone {}  and credit value {}",
                        employee.getPhoneNumber(),
                        employee.getUserCreditValue());
                readCreditVal = employee.getUserCreditValue();
            }
            // commit transaction
            hql = "UPDATE userInfo set userCreditValue = :userCreditValue WHERE phoneNumber= :userPhone";
            query = session.createQuery(hql);
            readCreditVal=readCreditVal+100;
            query.setParameter("userCreditValue", readCreditVal);
            query.setParameter("userPhone", phoneNumber);
            query.executeUpdate();
            transaction.commit();
            session.close();
        } catch (
                Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }

    }
}
