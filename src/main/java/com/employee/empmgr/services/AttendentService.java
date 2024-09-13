package com.employee.empmgr.services;

import com.employee.empmgr.model.Attendent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.DayOfWeek;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import java.time.format.DateTimeFormatter;
@Service
public class AttendentService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    public Duration calculateWorkingHoursForSelectedMonth(Long employeeId) {
        LocalDate firstDayOfMonth = YearMonth.now().atDay(1);
        LocalDate lastDayOfMonth = YearMonth.now().atEndOfMonth();

        List<Attendent> attendentList = entityManager.createQuery(
                "SELECT a FROM Attendent a " +
                        "WHERE a.employee.id = :employeeId " +
                        "AND a.createDate BETWEEN :firstDayOfMonth AND :lastDayOfMonth",
                Attendent.class)
                .setParameter("employeeId", employeeId)
                .setParameter("firstDayOfMonth", firstDayOfMonth)
                .setParameter("lastDayOfMonth", lastDayOfMonth)
                .getResultList();
       
        return calculateTotalWorkingHours(attendentList);
    }

    @Transactional(readOnly = true)
    public Duration calculateWorkingHoursForSelectedMonth(Long employeeId,String selectMonth) {
        YearMonth yearMonth = YearMonth.parse(selectMonth, DateTimeFormatter.ofPattern("yyyy-MM"));

        LocalDate firstDayOfMonth = yearMonth.atDay(1);
        LocalDate lastDayOfMonth = yearMonth.atEndOfMonth();

        List<Attendent> attendentList = entityManager.createQuery(
                "SELECT a FROM Attendent a " +
                        "WHERE a.employee.id = :employeeId " +
                        "AND a.createDate BETWEEN :firstDayOfMonth AND :lastDayOfMonth",
                Attendent.class)
                .setParameter("employeeId", employeeId)
                .setParameter("firstDayOfMonth", firstDayOfMonth)
                .setParameter("lastDayOfMonth", lastDayOfMonth)
                .getResultList();

        return calculateTotalWorkingHours(attendentList);
    }

    private Duration calculateTotalWorkingHours(List<Attendent> attendentList) {
        Duration totalWorkingHours = Duration.ZERO;

        for (int i = 0; i < attendentList.size(); i++) {
            Attendent startAttendent = attendentList.get(i);
            if ("start".equals(startAttendent.getStatus())) {
                LocalTime startTime = startAttendent.getCreateTime();
                for (int j = i + 1; j < attendentList.size(); j++) {
                    Attendent finishAttendent = attendentList.get(j);
                    if ("finish".equals(finishAttendent.getStatus()) &&
                            finishAttendent.getCreateDate().equals(startAttendent.getCreateDate())) {
                        LocalTime endTime = finishAttendent.getCreateTime();
                        totalWorkingHours = totalWorkingHours.plus(Duration.between(startTime, endTime));
                        break;
                    }
                }
            }
        }

        return totalWorkingHours;
    }
   

    public double calculateSalary(long totalActualWorkingHours, long totalDefaultWorkingHours, long HOURLY_RATE) {
       
        double salary = totalActualWorkingHours * HOURLY_RATE;
             
        if (totalActualWorkingHours > totalDefaultWorkingHours) {
            // Calculate bonus for each extra hour
            salary = totalDefaultWorkingHours * HOURLY_RATE;
            double extraHours = totalActualWorkingHours - totalDefaultWorkingHours;
           
            double bonus = extraHours * HOURLY_RATE * 2; // Bonus rate, e.g., 2 times the hourly rate
            salary += bonus;
            System.out.println("Bonus applied: " + bonus);
        } else if (totalActualWorkingHours < totalDefaultWorkingHours) {
            // Calculate fine for each deficit hour
            double deficitHours = totalDefaultWorkingHours - totalActualWorkingHours;
            double fine = deficitHours * HOURLY_RATE * 0.1; // Fine rate, e.g., 0.5 times the hourly rate
            salary -= fine;
            System.out.println("Fine applied: " + fine);
        }

        return salary;

    }

    public static int countDaysPassedExcludingSundays() {
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfMonth = YearMonth.from(today).atDay(1);
        LocalDate currentDate = today;
        int count = 0;

        while (currentDate.isAfter(firstDayOfMonth) || currentDate.isEqual(firstDayOfMonth)) {
            if (currentDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
                count++;
            }
            currentDate = currentDate.minusDays(1);
        }

        return count;
    }

    public static int countDaysPassedExcludingSundays(String selectMonth) {
         YearMonth yearMonth = YearMonth.parse(selectMonth, DateTimeFormatter.ofPattern("yyyy-MM"));

        LocalDate firstDayOfMonth = yearMonth.atDay(1);
        LocalDate currentDate = yearMonth.atEndOfMonth();
        int count = 0;

        while (currentDate.isAfter(firstDayOfMonth) || currentDate.isEqual(firstDayOfMonth)) {
            if (currentDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
                count++;
            }
            currentDate = currentDate.minusDays(1);
        }
        return count;
    }
  

}
