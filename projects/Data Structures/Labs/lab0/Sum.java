public class Sum
 {
 	public static void main(String[] args) {
 		int sum = 0;
 		for (String arg : args) {
 			try {
 				int localNumber = Integer.parseInt(arg);
 				sum += localNumber;
 			}
 			catch(Exception e) {
 				System.out.println("Please only input numbers.");
 			}
 		}
 		System.out.println(sum);
 	}
 }