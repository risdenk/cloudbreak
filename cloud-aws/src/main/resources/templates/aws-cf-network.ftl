<#setting number_format="computer">
{
  "AWSTemplateFormatVersion" : "2010-09-09",

  "Description" : "Deploys a Hortonworks Data Platform cluster on AWS.",

  "Resources" : {

    "VPC" : {
      "Type" : "AWS::EC2::VPC",
      "Properties" : {
        "CidrBlock" : "${vpcCidr}",
        "EnableDnsSupport" : "true",
        "EnableDnsHostnames" : "true",
        "Tags" : [
          { "Key" : "Application", "Value" : { "Ref" : "AWS::StackId" } },
          { "Key" : "Network", "Value" : "Public" },
          { "Key" : "cb-resource-type", "Value" : "${network_resource}" }
        ]
      }
    },

    <#list subnetCidrs as cidr>
    "PublicSubnet${cidr?index}" : {
      "Type" : "AWS::EC2::Subnet",
      "Properties" : {
        "MapPublicIpOnLaunch" : true,
        "CidrBlock" : "${cidr}",
        "VpcId" : { "Ref" : "VPC" },
        "Tags" : [
          { "Key" : "Application", "Value" : { "Ref" : "AWS::StackId" } },
          { "Key" : "Network", "Value" : "Public" },
          { "Key" : "cb-resource-type", "Value" : "${network_resource}" }
        ]
      }
    },
    </#list>

    "InternetGateway" : {
      "Type" : "AWS::EC2::InternetGateway",
      "Properties" : {
        "Tags" : [
          { "Key" : "Application", "Value" : { "Ref" : "AWS::StackId" } },
          { "Key" : "Network", "Value" : "Public" },
          { "Key" : "cb-resource-type", "Value" : "${network_resource}" }
        ]
      }
    },

    "AttachGateway" : {
       "Type" : "AWS::EC2::VPCGatewayAttachment",
       "Properties" : {
         "VpcId" : { "Ref" : "VPC" },
         "InternetGatewayId" : { "Ref" : "InternetGateway" }
       }
    },

    "PublicRouteTable" : {
      "Type" : "AWS::EC2::RouteTable",
      "Properties" : {
        "VpcId" : { "Ref" : "VPC" },
        "Tags" : [
          { "Key" : "Application", "Value" : { "Ref" : "AWS::StackId" } },
          { "Key" : "Network", "Value" : "Public" },
          { "Key" : "cb-resource-type", "Value" : "${network_resource}" }
        ]
      }
    },

    <#list subnetCidrs as cidr>
    "PublicSubnetRouteTableAssociation${cidr?index}" : {
      "Type" : "AWS::EC2::SubnetRouteTableAssociation",
      "Properties" : {
        "SubnetId" : { "Ref" : "PublicSubnet${cidr?index}" },
        "RouteTableId" : { "Ref" : "PublicRouteTable" }
      }
    },
    </#list>

    "PublicRoute" : {
          "Type" : "AWS::EC2::Route",
          "DependsOn" : [ "PublicRouteTable", "AttachGateway" ],
          "Properties" : {
            "RouteTableId" : { "Ref" : "PublicRouteTable" },
            "DestinationCidrBlock" : "0.0.0.0/0",
            "GatewayId" : { "Ref" : "InternetGateway" }
          }
    }
  },

  "Outputs" : {
    <#list subnetCidrs as cidr>
    "CreatedSubnets${cidr?index}" : {
        "Value" :  { "Ref" : "PublicSubnet${cidr?index}" }
    },
    </#list>
    "CreatedVpc": {
        "Value" : { "Ref" : "VPC" }
    }
  }
}