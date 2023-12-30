# Regilite (Registrate at home)
A light library helping with registration and data generation

## Content
This library provides helper functions and wrappers around for various Objects. Below we provide a list of all
functions provided.

### Translations
We provide automatic translations for objects where the `ResourceLocation` gives enough context. This can always 
be changed using the `setTranslation` method.

Besides the translation of objects like blocks and items, we also provide helpers for adding automatic translations for `Components`.

### Blocks
Besides the block itself, there are also functions to automatically register the following:
- Loot tables
- Blockstates and model
- Block color (tint)
- Block Tags
- Translation
- Block item 
  - See `Items`

### Items
Besides the item itself, there are also functions to automatically register the following:
- Item model
- Item color (tint)
- Item Tags
- Creative tabs
- Translation

### Fluid(type)s
Besides the fluid(type) itself, there are also functions to automatically register the following:
- Flowing fluid
- Source fluid
- Fluid tag
- Translation
- Liquid block
    - See `Block`
- Bucket Item
  - See `Item`

### Block Entity(type)s
Besides the block entity(type) itself, it automatically registers the following:

- Block entity renderer (should we?)
- Block entity tags

This helper meanly helps by removing a lot of (constant) generic bounds from the code, and provides a shortcut to
create a block entity directly from the holder.

### Entity(type)s
Besides the entity(type) itself, it automatically registers the following:

- Entity model (should we?)
- Model layer (should we?)
- Entity tags
- Translation

This helper also removes a lot of (constant) generic bounds from the code.

### Menu(type)s
Besides the menu(type) itself, it automatically registers the following:

- Screen factory (should we?)
- Translation (should we, as we don't use it?)

This helper also removes a lot of (constant) generic bounds from the code.
