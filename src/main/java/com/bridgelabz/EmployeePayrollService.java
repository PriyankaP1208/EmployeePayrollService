package com.bridgelabz;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EmployeePayrollService {

    public enum IOService {
        CONSOLE_IO, FILE_IO, DB_IO, REST_IO
    }

    private List<EmployeePayrollData> employeePayrollDataList;

    public EmployeePayrollService() {}

    public EmployeePayrollService(List<EmployeePayrollData> employeePayrollDataList) { }

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
            this.employeePayrollDataList = new EmployeePayrollDBService().readData();
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
