//select car model from drop down menu
import java.util.*;
import java.io.*;
public class EmissionsCalculatorNew
{
    double mass=0.0,distance=0.0,emission=1.0;
    int num=0;
    String make="";
    String GREEN="\u001B[32m", YELLOW="\u001B[33m", RED="\u001B[31m",RESET="\u001B[0m";
    String path="src\\Book1.csv";
    char ch,state;
    ArrayList<String> name=new ArrayList<>();
    ArrayList<Character> fueltype=new ArrayList<>();
    ArrayList<String> payload=new ArrayList<>();
    ArrayList<String> efficiency = new ArrayList<>();
    void input()//method to accept input
    {
        Scanner sc = new Scanner(System.in);
        System.out.println ("\t\tScope 3 Emissions calculator (based on mass carried and distance traveled):");//heading
        System.out.println ("\nEnter the number next to the car model that you would use: ");
        for (int i=0;i<name.size();i++)
        {
            System.out.println (i+". "+name.get(i));
        }
        System.out.print ("Enter the number corresponding to the model you would use: ");
        int x=sc.nextInt();
        make=name.get(x);
        System.out.print ("\nEnter weight of goods transported in tonnes: ");
        mass=sc.nextDouble()*1000.0;//tonnes -> kg
        if (mass<=3500)//if load carried is less than 3.5 tonnes, ie, a light goods vehicle
        {
            System.out.print ("\nEnter P for petrol or D for diesel engine: ");//asking if the carrier is a petrol or a diesel model
            ch=Character.toLowerCase(sc.next().charAt(0));
            state='l';//light goods vehicle
        }
        else//if load carried is greater than 3.5 tonnes, ie, a heavy goods vehicle
        {
            ch='d';//strictly diesel engine
            state='h';//heavy goods vehicle
        }
        System.out.print ("\nEnter distance transported: ");//asking for distance travelled
        distance=sc.nextDouble();
        sc.close();
        if (name.contains(make)==true)
        {
            num= name.indexOf(make);
            if (Character.toLowerCase(fueltype.get(num))==ch)
                System.out.println (GREEN+"We have the model on offer"+RESET);
            else
                System.out.println (RED+"Sorry! We do not offer the model requested."+RESET);
        }

    }
    void read()//method to read dataset imported as a csv file
    {
        String line="";
        try(BufferedReader br=new BufferedReader(new FileReader(path));)
        {
            while ((line=br.readLine())!=null)
            {
                String[] row=line.split(",");
                name.add(row[0].toLowerCase());
                fueltype.add(row[1].charAt(0));
                payload.add(row[2]);
                efficiency.add(row[3]);
            }
            br.close();//closes BufferedReader
        }
        catch (IOException e)
        {
            System.out.println (e);
        }
    }
    void calculate()//method to calculate emission in kg.CO2/tonne-km
    {
        emission=mass*distance;
        switch(ch)
        {
            case 'p':
                emission*=0.83;//assuming general emission factor of a petrol engine of an LGV is 0.83kg CO2/tonne-km
                break;
            case 'd':
                if (state=='l')
                    emission*=0.78;//assuming general emission factor of a petrol engine of an LGV is 0.78kg CO2/tonne-km
                else
                    emission*=0.154;//assuming general emission factor of a petrol engine of an LGV is 0.154kg CO2/tonne-km
                break;
            default:
                System.out.println (RED+"Error! Wrong Choice!"+RESET);
                System.exit(0);
        }
    }
    void display()//method to display emissions
    {
        Scanner s = new Scanner(System.in);
        System.out.printf ("Emissions= %.2fkg CO2.\n",emission);//displays emissions
        if (emission <=(50*distance))
            System.out.println (GREEN+"Thank you for your contribution. Keep it up!!"+RESET);
        else if (emission>(50*distance) && emission<=(200*distance))
            System.out.println (GREEN+"Good going!! In addition, please try to optimise logistics and reduce empty miles."+RESET);
        else if (emission>(200*distance) && emission<=(500*distance))
            System.out.println (YELLOW+"Do consider hybrids or biofuel compatible vehicles for your future logistics plans"+RESET);
        else
            System.out.println (RED+"Consider shifting to public transport or using EVs."+RESET);
        System.out.print ("\nHere could be some alternatives. ");
        recommend();
    }
    void recommend()//method to recommend alternatives
    {
        System.out.print ("May we recommend: ");
        ArrayList<Integer> load =new ArrayList<>();
        ArrayList<Double> eff =new ArrayList<>();
        for (String str : payload)
            load.add(Integer.parseInt(str));
        for (String str : efficiency)
            eff.add(Double.parseDouble(str));
        for (int j=0;j<payload.size();j++)
        {
            if ((load.get(num)-mass)>(load.get(j)-mass))//if a more optimum carrying capacity exists
            {
                if (eff.get(j)>eff.get(num))//if it is more efficient
                {
                    System.out.println ("\n"+name.get(j));
                    System.out.println (GREEN+"PROS: more efficient"+RESET);
                }
                else if (eff.get(j).equals(eff.get(num)))//if it is just as efficient
                {
                    System.out.println ("\n"+name.get(j));
                    System.out.println (YELLOW+"PROS: just as efficient but more optimum option"+RESET);
                }
                else//if it is less efficient
                {
                    System.out.println ("\n"+name.get(j));
                    System.out.println (RED+"CONS: less efficient but more optimum option"+RESET);
                }
            }
        }
    }
    public static void main(String args[])
    {
        EmissionsCalculatorNew ob = new EmissionsCalculatorNew();
        ob.read();//reads input from csv database
        ob.input();//takes input from user
        ob.calculate();//calculates emissions
        ob.display();//displays result
    }
}