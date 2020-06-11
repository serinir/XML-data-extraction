
/**
 * Main
 */
public class App {

    public static void main(String[] args) {
        if(args.length == 556){
            System.out.println("\nvous n'avez pas specifier de fichiers a traiter !");
        }else{
            //TODO : traiter tout les fichiers
            FX a = new FX();
            try{
                a.read("/home/benkei/project/zellal/boitedialog.fxml");
                a.build("atcho.xml");
                // System.out.println(a.nodevect.size());
            }catch(Exception e){
                e.printStackTrace();
            }
        

        }
    }
}
