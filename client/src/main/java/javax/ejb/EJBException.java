package javax.ejb;

public class EJBException extends RuntimeException
{
   private static final long serialVersionUID = 796770993296843510L;
   private Exception causeException;
   
   public EJBException() {
      super();
      causeException = null;
   }
   
   public EJBException(String message) {
      super(message);
      causeException = null;
   }

   public EJBException(Exception ex) {
      super(ex);
      causeException = ex;
   }

   public EJBException(String message, Exception ex) {
      super(message, ex);
      causeException = ex;
   }

   public Exception getCausedByException() {
      return causeException;
   }
}