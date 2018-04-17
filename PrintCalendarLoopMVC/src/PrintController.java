import java.util.InputMismatchException;
import java.util.Scanner;

public class PrintController {

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);

		Character cmd = 'm';
		SolarMonth sm = null;
		boolean isEnglish = true;
		do {
			cmd = Character.toLowerCase(cmd);
			int year, month;
			switch (cmd) {
			case 'm':
				try {
					System.out.print("Enter full year (e.g., 2001): ");
					year = input.nextInt();
					System.out.print("Enter month in number between 1 and 12: ");
					month = input.nextInt();
					if (month < 1 || month > 12)
						throw new Exception("月份值超出范围[1-12]");
				} catch (InputMismatchException ex) {
					input.nextLine();
					System.out.println("不是整型值");
					continue;
				} catch (Exception ex) {
					input.nextLine();
					System.out.println(ex.getMessage());
					continue;
				}
				sm = new SolarMonth(year, month);
				break;
			case 'p':
				sm = sm.createPreviousMonth();
				break;
			case 'n':
				sm = sm.createNextMonth();
				break;
			case 'h':
				isEnglish = !isEnglish;
				break;
			}
			if (cmd == 'q') {
				System.out.println("End.");
				break;
			}
			PrintView view = isEnglish ? new PrintView(sm)
					: new HzPrintView(sm);
			view.printMonth();
			System.out.println();
			System.out.println("-----------------------------");
			System.out.println("M - 显示指定的年月份日历");
			System.out.println("P - 显示前一月份日历");
			System.out.println("N - 显示后一月份日历");
			System.out.printf("H - 转换打印语种[%s]\n", isEnglish ? "英文" : "汉语");
			System.out.println("Q - 退出");
			System.out.print("输入功能字符并按回车:");
			String s = input.next();
			cmd = s.charAt(0);
		} while (true);

		input.close();

	}
}

class PrintView {
	SolarMonth solarMonth;

	public PrintView(SolarMonth solarMonth) {
		this.solarMonth = solarMonth;
	}

	/** Print the calendar for a month in a year */
	public void printMonth() {
		// Print the headings of the calendar
		printMonthTitle();
		// Print the body of the calendar
		printMonthBody();
	}

	/** Print the month title, e.g., May, 1999 */
	public void printMonthTitle() {
		System.out.println("\n         " + solarMonth.getMonthName() + " "
				+ solarMonth.getYear());
		System.out.println("-----------------------------");
		System.out.println(" Sun Mon Tue Wed Thu Fri Sat");
	}

	/** Print month body */
	public void printMonthBody() {
		// Get start day of the week for the first date in the month
		int startDay = solarMonth.getStartDay();

		// Get number of days in the month
		int numberOfDaysInMonth = solarMonth.getNumberOfDaysInMonth();

		int i = 0;

		// Pad space before the first day of the month
		for (i = 0; i < startDay; i++)
			System.out.print("    ");
		for (i = 1; i <= numberOfDaysInMonth; i++) {
			System.out.printf("%4d", i);

			if ((i + startDay) % 7 == 0)
				System.out.println();
		}

		System.out.println();
	}

}

class HzPrintView extends PrintView {
	public HzPrintView(SolarMonth solarMonth) {
		super(solarMonth);
	}

	@Override
	public void printMonthTitle() {
		// TODO Auto-generated method stub
		System.out.println("\n        " + solarMonth.getYear() + "年"
				+ solarMonth.getMonth() + "月");
		System.out.println("-----------------------------");
		System.out.println("  日  一  二  三  四  五  六");

	}
}

class SolarMonth {
	private final static String[] names = { "", "January", "February", "March",
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
		return names[month];
	}

	/** Get the start day of month/1/year */
	// 此方法的计算量比较大。
	// 如果在速度上要求高,先将getStartDay()改名为findStartDay()，
	// 然后定义私有成员startDay并定义其访问器方法
	// public int getStartDay(){return startDay;}，
	// 在构造函数中调用findStartDay()方法初始化成员startDay，即
	// startDay=findStartDay();
	// 这是，用空间换时间，当前方法是，用时间换空间
	public int getStartDay() {
		final int START_DAY_FOR_JAN_1_1800 = 3;
		// Get total number of days from 1/1/1800 to month/1/year
		int totalNumberOfDays = getTotalNumberOfDays();

		// Return the start day for month/1/year
		return (totalNumberOfDays + START_DAY_FOR_JAN_1_1800) % 7;
	}

	/** Get the total number of days since January 1, 1800 */
	// 因为在'since January 1, 1800'条件下，该方法才有意义，
	// 所以定义为所有的
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
	// 因为该方法使用非本对象成员变量方法，
	// 所以保留该静态方法，同时定义了相应的实例方法
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