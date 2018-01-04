package taotao.com.item.pojo;

import taotao.com.pojo.TbItem;

public class Item extends TbItem {
	public Item(TbItem tbItem){
		this.setId(tbItem.getId());
		this.setBarcode(tbItem.getBarcode());
		this.setCreated(tbItem.getCreated());
		this.setUpdated(tbItem.getUpdated());
		this.setNum(tbItem.getNum());
		this.setPrice(tbItem.getPrice());
		this.setSellPoint(tbItem.getSellPoint());
		this.setStatus(tbItem.getStatus());
		this.setTitle(tbItem.getTitle());
		this.setImage(tbItem.getImage());
		this.setCid(tbItem.getCid());
	}
	
	public String[] getImages(){
		if(this.getImage()!=null && !"".equals(this.getImage())){
			String image=this.getImage();
			String[] result=image.split(",");
			return result;
		}
		return null;
	}
}
