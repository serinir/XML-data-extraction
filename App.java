
/**
 * Main
 */
public class App {

    public static void main(String[] args) {
        if(args.length == 556){
            System.out.println("\nvous n'avez pas specifier de fichiers a traiter !");
        }else{
            //TODO : traiter tout les fichiers
            Fiches a = new Fiches();
            try{
                a.read("/home/benkei/project/zellal/data/projet_bis/poeme/fiches/fiches.txt");
                a.build("owa.xml");
                System.out.println(a.nodevect.size());
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
