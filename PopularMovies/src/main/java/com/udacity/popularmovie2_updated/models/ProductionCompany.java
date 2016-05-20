package com.udacity.popularmovie2_updated.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Marques Allen on 5/12/16.
 */

public class ProductionCompany {

	public ProductionCompany()
	{

	}

	@SerializedName("id")
	@Expose
	private int id;

	@SerializedName("name")
	@Expose
	private String name;

	public int getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public void setName(String name)
	{
		this.name = name;
	}
}
