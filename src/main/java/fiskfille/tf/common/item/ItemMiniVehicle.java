package fiskfille.tf.common.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fiskfille.tf.TransformersAPI;
import fiskfille.tf.TransformersMod;
import fiskfille.tf.common.transformer.base.Transformer;

public class ItemMiniVehicle extends Item implements IDisplayPillarItem
{
	public ItemMiniVehicle()
	{
		super();
		this.setMaxStackSize(1);
		this.setCreativeTab(TransformersMod.tabTransformers);
		this.setHasSubtypes(true);
	}

	public String getItemStackDisplayName(ItemStack stack)
	{
		Transformer transformer = TransformersAPI.getTransformers().get(stack.getItemDamage());

		if (transformer != null)
		{
			return StatCollector.translateToLocal("item.display_" + transformer.getName().toLowerCase().replaceAll(" ", "_") + ".name");
		}
		else
		{
			return super.getItemStackDisplayName(stack);
		}
	}

	public void addInformation(ItemStack itemstack, EntityPlayer player, List list, boolean p_77624_4_)
	{
		list.add("Equippable");
	}

	public void setNBTData(ItemStack itemstack)
	{
		int transformerIndex = 0;
		
		for (Transformer transformer : TransformersAPI.getTransformers())
		{
			if (transformerIndex == itemstack.getItemDamage())
			{
				ItemStack head = new ItemStack(transformer.getHelmet());
				ItemStack chest = new ItemStack(transformer.getChestplate());
				ItemStack legs = new ItemStack(transformer.getLeggings());
				ItemStack feet = new ItemStack(transformer.getBoots());
				ItemStack[] itemstacks = {head, chest, legs, feet};
				NBTTagList itemsList = new NBTTagList();

				for (int i = 0; i < itemstacks.length; ++i)
				{
					if (itemstacks[i] != null)
					{
						NBTTagCompound itemTag = new NBTTagCompound();
						itemTag.setByte("Slot", (byte)i);
						itemstacks[i].writeToNBT(itemTag);
						itemsList.appendTag(itemTag);
					}
				}

				itemstack.getTagCompound().setTag("Items", itemsList);
			}
			
			transformerIndex++;
		}
	}

	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player)
	{
		if (!itemstack.hasTagCompound())
		{
			itemstack.setTagCompound(new NBTTagCompound());
		}

		if (player.isSneaking())
		{
			if (getArmorFromNBT(itemstack) == null)
			{
				setNBTData(itemstack);
			}

			if (getArmorFromNBT(itemstack)[0] != null)
			{
				if (!player.worldObj.isRemote)
				{
					if (player.getCurrentArmor(3) != null)
					{
						player.entityDropItem(player.getCurrentArmor(3), 0);
					}
				}

				player.setCurrentItemOrArmor(4, getArmorFromNBT(itemstack)[0]);
			}
			if (getArmorFromNBT(itemstack)[1] != null)
			{
				if (!player.worldObj.isRemote)
				{
					if (player.getCurrentArmor(2) != null)
					{
						player.entityDropItem(player.getCurrentArmor(2), 0);
					}
				}

				player.setCurrentItemOrArmor(3, getArmorFromNBT(itemstack)[1]);
			}
			if (getArmorFromNBT(itemstack)[2] != null)
			{
				if (!player.worldObj.isRemote)
				{
					if (player.getCurrentArmor(1) != null)
					{
						player.entityDropItem(player.getCurrentArmor(1), 0);
					}
				}

				player.setCurrentItemOrArmor(2, getArmorFromNBT(itemstack)[2]);
			}
			if (getArmorFromNBT(itemstack)[3] != null)
			{    			
				if (!player.worldObj.isRemote)
				{
					if (player.getCurrentArmor(0) != null)
					{
						player.entityDropItem(player.getCurrentArmor(0), 0);
					}
				}

				player.setCurrentItemOrArmor(1, getArmorFromNBT(itemstack)[3]);
			}

			player.setCurrentItemOrArmor(0, null);
		}

		return itemstack;
	}

	public ItemStack[] getArmorFromNBT(ItemStack itemstack)
	{
		if (itemstack.hasTagCompound() && itemstack.getTagCompound().hasKey("Items"))
		{
			NBTTagList nbttaglist = itemstack.getTagCompound().getTagList("Items", 10);
			ItemStack[] itemstacks = new ItemStack[4];

			for (int i = 0; i < nbttaglist.tagCount(); ++i)
			{
				NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.getCompoundTagAt(i);
				byte b0 = nbttagcompound1.getByte("Slot");

				if (b0 >= 0 && b0 < itemstacks.length)
				{
					itemstacks[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
				}
			}

			return itemstacks;
		}

		return null;
	}

	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister) {}

	public void getSubItems(Item item, CreativeTabs tab, List subItems)
	{
		int i = 0;
		
		for (Transformer transformer : TransformersAPI.getTransformers())
		{
			subItems.add(new ItemStack(this, 1, i));
			i++;
		}
	}
}