package image.google;

import android.os.Parcel;
import android.os.Parcelable;

public class GoogleImage implements Parcelable {
	// image Id
	private String imageId;
	// ½æ³×ÀÏ 
	private String tbUrl;
    private String tbHeight;
    private String tbWidth;
    // ¿øº» 
    private String unescapedUrl;
    private String height;
    private String width;
    
    public static final Parcelable.Creator<GoogleImage> CREATOR = new Parcelable.Creator<GoogleImage>() {
        public GoogleImage createFromParcel(Parcel in) {
            return new GoogleImage(in);
        }

        public GoogleImage[] newArray(int size) {
            return new GoogleImage[size];
        }
    };
    
    public GoogleImage(String imageId, String tbUrl, String tbHeight, String tbWidth,
    		String unescapedUrl, String height, String width) {
    	this.imageId = imageId;
    	this.tbUrl = tbUrl;
    	this.tbHeight = tbHeight;
    	this.tbWidth = tbWidth;
    	this.unescapedUrl = unescapedUrl;
    	this.height = height;
    	this.width  = width;
    }
    public GoogleImage(Parcel in) {
		imageId = in.readString();
		tbUrl = in.readString();
	    tbHeight = in.readString();
	    tbWidth = in.readString();
	    unescapedUrl = in.readString();
	    height = in.readString();
	    width = in.readString();
    }
    
    // getter & setter
    public String getImageId() { return imageId; }
	public void setImageId(String imageId) { this.imageId = imageId; }
	public String getTbUrl() { return tbUrl; }
	public void setTbUrl(String tbUrl) { this.tbUrl = tbUrl; }
	public String getTbHeight() { return tbHeight; }
	public void setTbHeight(String tbHeight) { this.tbHeight = tbHeight; }
	public String getTbWidth() { return tbWidth; }
	public void setTbWidth(String tbWidth) { this.tbWidth = tbWidth; }
	public String getUnescapedUrl() { return unescapedUrl; }
	public void setUnescapedUrl(String unescapedUrl) { this.unescapedUrl = unescapedUrl; }
	public String getHeight() { return height; }
	public void setHeight(String height) { this.height = height; }
	public String getWidth() { return width; }
	public void setWidth(String width) { this.width = width; }

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeString(imageId);
		parcel.writeString(tbUrl);
		parcel.writeString(tbHeight);
		parcel.writeString(tbWidth);
		parcel.writeString(unescapedUrl);
		parcel.writeString(height);
		parcel.writeString(width);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((height == null) ? 0 : height.hashCode());
		result = prime * result + ((imageId == null) ? 0 : imageId.hashCode());
		result = prime * result
				+ ((tbHeight == null) ? 0 : tbHeight.hashCode());
		result = prime * result + ((tbUrl == null) ? 0 : tbUrl.hashCode());
		result = prime * result + ((tbWidth == null) ? 0 : tbWidth.hashCode());
		result = prime * result
				+ ((unescapedUrl == null) ? 0 : unescapedUrl.hashCode());
		result = prime * result + ((width == null) ? 0 : width.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GoogleImage other = (GoogleImage) obj;
		if (height == null) {
			if (other.height != null)
				return false;
		} else if (!height.equals(other.height))
			return false;
		if (imageId == null) {
			if (other.imageId != null)
				return false;
		} else if (!imageId.equals(other.imageId))
			return false;
		if (tbHeight == null) {
			if (other.tbHeight != null)
				return false;
		} else if (!tbHeight.equals(other.tbHeight))
			return false;
		if (tbUrl == null) {
			if (other.tbUrl != null)
				return false;
		} else if (!tbUrl.equals(other.tbUrl))
			return false;
		if (tbWidth == null) {
			if (other.tbWidth != null)
				return false;
		} else if (!tbWidth.equals(other.tbWidth))
			return false;
		if (unescapedUrl == null) {
			if (other.unescapedUrl != null)
				return false;
		} else if (!unescapedUrl.equals(other.unescapedUrl))
			return false;
		if (width == null) {
			if (other.width != null)
				return false;
		} else if (!width.equals(other.width))
			return false;
		return true;
	}
}
