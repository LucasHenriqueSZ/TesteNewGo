interface component {

    getSize(): number;
    getName(): string;
    getType(): string;
    addComponent(component: component): void;
    removeComponent(component: String): void;
    getComponent(name: String): component | null;
    
}