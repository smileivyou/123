import java.util.Scanner;

public class PrintCalendarOO {

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);

		Character cmd = 'm';
		SolarMonth sm = null;
		do {
			cmd = Character.toLowerCase(cmd);
			int year, month;
			switch (cmd) {
			case 'm':
				System.out.print("Enter full year (e.g., 2001): ");
				year = input.nextInt();
				System.out.print("Enter month in number between 1 and 12: ");
				month = input.nextInt();
				sm = new SolarMonth(year, month);
				break;
			case 'p':
				sm = sm.createPreviousMonth();
				break;
			case 'n':
				sm = sm.createNextMonth();
				break;
			}
			if (cmd != 'q'){
				printMonth(sm);
				System.out.println("M - ��ʾָ�������·�����");
				System.out.println("P  - ��ʾǰһ�·�����");
				System.out.println("N - ��ʾ��һ�·�����");
				System.out.println("Q - �˳�");
				System.out.print("���빦���ַ������س�:");
				String s=input.next().trim();
				cmd=s.charAt(0);
			}	else {
				System.out.println("End.");
				break;
			}
		} while (true);

		input.close();

	}

	/** Print the calendar for a month in a year */
	public static void printMonth(SolarMonth cm) {
		// Print the headings of the calendar
		printMonthTitle(cm);
		// Print the body of the calendar
		printMonthBody(cm);
	}

	/** Print the month title, e.g., May, 1999 */
	public static void printMonthTitle(SolarMonth cm) {
		System.out
				.println("         " + cm.getMonthName() + " " + cm.getYear());
		System.out.println("-----------------------------");
		System.out.println(" Sun Mon Tue Wed Thu Fri Sat");
	}

	/** Print month body */
	public static void printMonthBody(SolarMonth cm) {
		// Get start day of the week for the first date in the month
		int startDay = cm.getStartDay();

		// Get number of days in the month
		int numberOfDaysInMonth = cm.getNumberOfDaysInMonth();

		int i = 0;

		// Pad space before the first day of the month
		for (i = 0; i < startDay; i++)
			System.out.print("    ");
		// ������ǰ�����begin
		// SolarMonth prevMonth=cm.createPreviousMonth();
		// int numberOfDaysInPrevMonth=prevMonth.getNumberOfDaysInMonth();
		// for (i = startDay - 1; i >= 0; i--)
		// System.out.printf("%4d",numberOfDaysInPrevMonth - i);
		// end.
		for (i = 1; i <= numberOfDaysInMonth; i++) {
			System.out.printf("%4d", i);

			if ((i + startDay) % 7 == 0)
				System.out.println();
		}

		// ��β���������begin
		// int endDay = cm.getEndDay();
		// for (int d = endDay + 1; d <= 6; d++) {
		// System.out.printf("%4d", d - endDay);
		// }
		// end.

		System.out.println();
	}

}

class SolarMonth {
	private final static String[] names = { "January", "February", "March",
			"April", "May", "June", "July", "August", "September", "October",
			"November", "December" };
	private int year;
	private int month;

	public int getYear() {
		return year;
	}

	public int getMonth() {
		return month;
	}

	public SolarMonth(int year, int month) {
		if (month < 1)
			month = 1;
		if (month > 12)
			month = 12;
		this.year = year;
		this.month = month;
	}

	/** Get the English name for the month */
	public String getMonthName() {
		return names[month - 1];
	}

	/** Get the start day of month/1/year */
	// �˷����ļ������Ƚϴ�
	// ������ٶ���Ҫ���,�Ƚ�getStartDay()����ΪfindStartDay()��
	// Ȼ����˽�г�ԱstartDay�����������������
	// public int getStartDay(){return startDay;}��
	// �ڹ��캯���е���findStartDay()������ʼ����ԱstartDay����
	// startDay=findStartDay();
	// ���ǣ��ÿռ任ʱ�䣬��ǰ�����ǣ���ʱ�任�ռ�
	public int getStartDay() {
		final int START_DAY_FOR_JAN_1_1800 = 3;
		// Get total number of days from 1/1/1800 to month/1/year
		int totalNumberOfDays = getTotalNumberOfDays();

		// Return the start day for month/1/year
		return (totalNumberOfDays + START_DAY_FOR_JAN_1_1800) % 7;
	}

	/** Get the end day of month */
	public int getEndDay() {
		// Return the end day for month
		return (getStartDay() + getNumberOfDaysInMonth() - 1) % 7;
	}

	/** Get the total number of days since January 1, 1800 */
	// ��Ϊ��'since January 1, 1800'�����£��÷����������壬
	// ���Զ���Ϊ���е�
	private int getTotalNumberOfDays() {
		int total = 0;

		// Get the total days from 1800 to 1/1/year
		for (int i = 1800; i < year; i++)
			if (isLeapYear(i))
				total = total + 366;
			else
				total = total + 365;

		// Add days from Jan to the month prior to the calendar month
		for (int i = 1; i < month; i++)
			total = total + getNumberOfDaysInMonth(year, i);

		return total;
	}

	/** Get the number of days in a month */
	public int getNumberOfDaysInMonth() {
		return getNumberOfDaysInMonth(year, month);
	}

	/** Get the number of days in a month */
	// ��Ϊ�÷���ʹ�÷Ǳ������Ա����������
	// ���Ա����þ�̬������ͬʱ��������Ӧ��ʵ������
	public static int getNumberOfDaysInMonth(int year, int month) {
		if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8
				|| month == 10 || month == 12)
			return 31;

		if (month == 4 || month == 6 || month == 9 || month == 11)
			return 30;

		if (month == 2)
			return isLeapYear(year) ? 29 : 28;

		return 0; // If month is incorrect
	}

	/** Determine if it is a leap year */
	public static boolean isLeapYear(int year) {
		return year % 400 == 0 || (year % 4 == 0 && year % 100 != 0);
	}

	/** Get the next month */
	public SolarMonth createNextMonth() {
		int nexty, nextm;
		if (month < 12) {
			nexty = year;
			nextm = month + 1;
		} else {
			nexty = year + 1;
			nextm = 1;
		}
		return new SolarMonth(nexty, nextm);
	}

	/** Get the previous month */
	public SolarMonth createPreviousMonth() {
		int prevy, prevm;
		if (month > 1) {
			prevy = year;
			prevm = month - 1;
		} else {
			prevy = year - 1;
			prevm = 12;
		}
		return new SolarMonth(prevy, prevm);
	}

}