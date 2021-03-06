/*
 * Copyright (c) 2018-2019 Qualcomm Technologies, Inc.
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, are permitted (subject to the limitations in the disclaimer below) provided that the following conditions are met:
 *  Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *  Neither the name of Qualcomm Technologies, Inc. nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 *  The origin of this software must not be misrepresented; you must not claim that you wrote the original software. If you use this software in a product, an acknowledgment is required by displaying the trademark/log as per the details provided here: [https://www.qualcomm.com/documents/dirbs-logo-and-brand-guidelines]
 *  Altered source versions must be plainly marked as such, and must not be misrepresented as being the original software.
 *  This notice may not be removed or altered from any source distribution.
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.qualcomm.dvsauthorized.mModels.imeiResponseModel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

// Model class for Gsma data
public class Gsma implements Serializable {

	@SerializedName("model_name")
	private String modelName;

	@SerializedName("radio_access_technology")
	private String radioAccessTechnology;

	@SerializedName("operating_system")
	private Object operatingSystem;

	@SerializedName("device_type")
	private String deviceType;

	@SerializedName("model_number")
	private String modelNumber;

	@SerializedName("brand")
	private String brand;

	@SerializedName("manufacturer")
	private String manufacturer;

	public void setModelName(String modelName){
		this.modelName = modelName;
	}

	public String getModelName(){
		return modelName;
	}

	public void setRadioAccessTechnology(String radioAccessTechnology){
		this.radioAccessTechnology = radioAccessTechnology;
	}

	public String getRadioAccessTechnology(){
		return radioAccessTechnology;
	}

	public void setOperatingSystem(Object operatingSystem){
		this.operatingSystem = operatingSystem;
	}

	public Object getOperatingSystem(){
		return operatingSystem;
	}

	public void setDeviceType(String deviceType){
		this.deviceType = deviceType;
	}

	public String getDeviceType(){
		return deviceType;
	}

	public void setModelNumber(String modelNumber){
		this.modelNumber = modelNumber;
	}

	public String getModelNumber(){
		return modelNumber;
	}

	public void setBrand(String brand){
		this.brand = brand;
	}

	public String getBrand(){
		return brand;
	}

	public void setManufacturer(String manufacturer){
		this.manufacturer = manufacturer;
	}

	public String getManufacturer(){
		return manufacturer;
	}

	@Override
 	public String toString(){
		return 
			"Gsma{" + 
			"model_name = '" + modelName + '\'' + 
			",radio_access_technology = '" + radioAccessTechnology + '\'' + 
			",operating_system = '" + operatingSystem + '\'' + 
			",device_type = '" + deviceType + '\'' + 
			",model_number = '" + modelNumber + '\'' + 
			",brand = '" + brand + '\'' + 
			",manufacturer = '" + manufacturer + '\'' + 
			"}";
		}
}