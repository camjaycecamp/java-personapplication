
public class OCCCPerson  extends RegisteredPerson
{
	private String studentID;
	
	
	public OCCCPerson(RegisteredPerson p, String studentID) 
	{
		super(p);
		this.studentID = studentID;
	}
	
	public OCCCPerson(OCCCPerson oP)
	{
		super(oP.getFirstName(), oP.getLastName(), oP.getGovernmentID());
		this.studentID = oP.getStudentID();
	}

	public String getStudentID() 
	{
		return studentID;
	}
	
	public boolean equals(OCCCPerson p) 
	{
		return getFirstName().equalsIgnoreCase(p.getFirstName()) &&
	             getLastName().equalsIgnoreCase(p.getLastName()) &&
	             getGovernmentID().equalsIgnoreCase(p.getGovernmentID()) &&
	             studentID.equalsIgnoreCase(p.studentID);
	}
	
	public boolean equals(RegisteredPerson p) 
	{
		return getFirstName().equalsIgnoreCase(p.getFirstName()) &&
	             getLastName().equalsIgnoreCase(p.getLastName()) &&
	             getGovernmentID().equalsIgnoreCase(p.getGovernmentID());
	}
	
	public boolean equals(Person p) 
	{
		return getFirstName().equalsIgnoreCase(p.getFirstName()) &&
	             getLastName().equalsIgnoreCase(p.getLastName());
	}
	
	public String toString() 
	{
		return super.toString() + " [" + studentID + "]";
	}
}
