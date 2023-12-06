import java.io.*;
import java.util.ArrayList;

public class AgendaTelefoneitor implements Serializable {
    private static final long serialVersionUID = 1L;

    private ArrayList<Contacto> contactos;

    public AgendaTelefoneitor() {
        contactos = new ArrayList<>();
        cargarContactos();
    }

    public void agregarContacto(Contacto contacto) {
        contactos.add(contacto);
        guardarContactos();
    }

    public ArrayList<Contacto> getContactos() {
        return contactos;
    }

    public void guardarContactos() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("contactos.dat"))) {
            oos.writeObject(contactos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @SuppressWarnings("unchecked")
    private void cargarContactos() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("contactos.dat"))) {
            contactos = (ArrayList<Contacto>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
