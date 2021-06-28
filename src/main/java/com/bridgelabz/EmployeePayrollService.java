package com.bridgelabz;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EmployeePayrollService {
    private EmployeePayrollDBService employeePayrollDBService;

    public void updateEmployeeSalary(String name, double salary) {
        int result = employeePayrollDBService.updateEmployeeData(name, salary);
        if(result == 0)
            return;
        EmployeePayrollData employeePayrollData = this.getEmployeePayrollData(name);
        if(employeePayrollData != null)
            employeePayrollData.salary = salary;
    }

    private EmployeePayrollData getEmployeePayrollData(String name) {
        EmployeePayrollData employeePayrollData;
        employeePayrollData = this.employeePayrollDataList.stream()
                .filter(employeePayrollDataItem -> employeePayrollDataItem.name.equals(name))
                .findFirst()
                .orElse(null);
        return employeePayrollData;
    }

    public boolean checkEmployeePayrollInSyncWithDB(String name) {
        List<EmployeePayrollData> employeePayrollDataList = employeePayrollDBService.getEmployeePayrollData(name);
        return employeePayrollDataList.get(0).equals(getEmployeePayrollData(name));
    }

    public List<EmployeePayrollData> readEmployeePayrollDataRange(IOService ioService,
                                                                  LocalDate startDate, LocalDate endDate) {
        if(ioService.equals(IOService.DB_IO))
            return employeePayrollDBService.getEmployeePayrollForDateRange(startDate, endDate);
        return null;
    }

    public enum IOService {
        CONSOLE_IO, FILE_IO, DB_IO, REST_IO
    }

    private List<EmployeePayrollData> employeePayrollDataList;

    public EmployeePayrollService() {
        employeePayrollDBService = EmployeePayrollDBService.getInstance();
    }

    public EmployeePayrollService(List<EmployeePayrollData> employeePayrollDataList) {
        this();
        this.employeePayrollDataList = employeePayrollDataList;
    }

    public static void main(String args[]) {
        ArrayList<EmployeePayrollData> employeePayrollList = new ArrayList<EmployeePayrollData>();
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        Scanner consoleInputReader = new Scanner(System.in);
        employeePayrollService.readEmployeePayrollData(consoleInputReader);
        employeePayrollService.writeEmployeePayrollData(IOService.CONSOLE_IO);
    }

    private void readEmployeePayrollData (Scanner consoleInputReader)
    {
        System.out.println("Enter Employee Id:");
        int id = consoleInputReader.nextInt();
        System.out.println("Enter Employee Name:");
        String name = consoleInputReader.nextLine();
        System.out.println("Enter Employee Salary:");
        double salary = consoleInputReader.nextDouble();
        employeePayrollDataList.add(new EmployeePayrollData(id, name, salary));
    }

    public List<EmployeePayrollData> readEmployeePayrollData(IOService ioService)
    {
        if(ioService.equals(IOService.DB_IO))
            this.employeePayrollDataList = employeePayrollDBService.readData();
        return this.employeePayrollDataList;
    }

    private void writeEmployeePayrollData(IOService ioService)
    {
        if(ioService.equals(IOService.CONSOLE_IO))
            System.out.println("\n Writing Employee Payroll Roaster to Console");
        else if (ioService.equals(IOService.FILE_IO))
            new EmployeePayrollFileIOService().writeData(employeePayrollDataList);
    }

    public long countEntries(IOService ioService) {
        if (ioService.equals(IOService.CONSOLE_IO))
            return employeePayrollDataList.size();
        else if (ioService.equals(IOService.FILE_IO))
            return EmployeePayrollFileIOService.countEntries();
        return 0;
    }

    public static void printData(IOService ioService){
        if (ioService.equals(IOService.FILE_IO)){
            new EmployeePayrollFileIOService().printDataFromFile();
        }
    }

    public void readDataFromFile(IOService ioService){
        if(ioService.equals(IOService.CONSOLE_IO)){
            new EmployeePayrollFileIOService().readDataFromFile();
        }
    }
}
