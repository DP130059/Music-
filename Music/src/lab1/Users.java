package lab1;

import java.io.Serializable;

public class Users  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long userid;
	private Long artistid;
	public Users(Long userid, Long artistid) {
		this.userid = userid;
		this.artistid = artistid;
	}
	public Long getUserid() {
		return userid;
	}
	public void setUserid(Long userid) {
		this.userid = userid;
	}
	public Long getArtistid() {
		return artistid;
	}
	public void setArtistid(Long artistid) {
		this.artistid = artistid;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(userid);
		builder.append(" ");
		builder.append(artistid);
		return builder.toString();
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((artistid == null) ? 0 : artistid.hashCode());
		result = prime * result + ((userid == null) ? 0 : userid.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Users)) {
			return false;
		}
		Users other = (Users) obj;
		if (artistid == null) {
			if (other.artistid != null) {
				return false;
			}
		} else if (!artistid.equals(other.artistid)) {
			return false;
		}
		if (userid == null) {
			if (other.userid != null) {
				return false;
			}
		} else if (!userid.equals(other.userid)) {
			return false;
		}
		return true;
	}
	

}
