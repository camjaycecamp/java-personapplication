
public class RegisteredPerson extends Person
{
	private String govID;
	
	
	public RegisteredPerson(String firstName, String lastName, String govID)
	{
		super(firstName, lastName);
		this.govID = govID;
	}
	
	public RegisteredPerson(Person p, String govID)
	{
		super(p);
		this.govID = govID;
	}
	
	public RegisteredPerson(RegisteredPerson rP)
	{
		super(rP.getFirstName(), rP.getFirstName());
		this.govID = rP.getGovernmentID();
	}

	
	public String getGovernmentID() 
	{
		return govID;
	}
	
	
	public boolean equals(RegisteredPerson p) 
	{
		return getFirstName().equalsIgnoreCase(p.getFirstName()) &&
	             getLastName().equalsIgnoreCase(p.getLastName()) &&
	             govID.equalsIgnoreCase(p.govID);
	}
	
	public boolean equals(Person p) 
	{
		return getFirstName().equalsIgnoreCase(p.getFirstName()) &&
	             getLastName().equalsIgnoreCase(p.getLastName());
	}
	
	@Override
	public String toString()
	{
	    return super.toString() + " [" + govID + "]";
	}
}
