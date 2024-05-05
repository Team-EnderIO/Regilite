# Regilite
A lightweight library helping with registration and data generation.

## Usage
This library provides helper functions and wrappers around for various Holders and registries. To be able to use 
Regilite, you firstly create and register a `Regilite`-object. This object will do the actual registring data to events 
and doing DataGen.

Below you can find a list of Holder objects Regilite supports. You can see not every Holder has a Regilite alternative, 
and this is intentional. We've only provided our wrappers when the Holder could benefit from it in our eyes.

### RegiliteBlock
Besides the block itself, there are also functions to automatically register the following:
- Loot tables
- Blockstates and model
- Block color (tint)
- Block Tags
- Translation
- Block item 
  - See `Items`

### RegiliteItem
Besides the item itself, there are also functions to automatically register the following:
- Item model
- Item color (tint)
- Item Tags
- Creative tabs
- Translation
- Capabilities

### RegiliteFluid
Besides the fluid(type) itself, there are also functions to automatically register the following:
- Flowing fluid
- Source fluid
- Fluid tag
- Translation
- Liquid block
    - See `Block`
- Bucket Item
  - See `Item`

### RegiliteBlockEntity
Besides the block entity(type) itself, it automatically registers the following:

- Block entity renderer
- Block entity tags
- Capabilities

### RegiliteEntity
Besides the entity(type) itself, it automatically registers the following:

- Entity model
- Entity tags
- Translation
- Capabilities (TODO)
- Model layer (TODO)


### RegiliteMenu
Besides the menu(type) itself, it automatically registers the following:

- Screen factory
- Translation (TODO)

### Translations
We provide automatic translations for holders where the `ResourceLocation` gives enough context. This can always
be changed using the `setTranslation` method.

For other translations, the `Regilite`-object provides methods for registering a translation. In case a translation is 
added multiple times, only the last one will be used.