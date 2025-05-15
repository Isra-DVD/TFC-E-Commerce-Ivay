export const colors = {
    primary: {
        main: "#9011BA",
        light: "#4822C0",
        dark: "#6c1b87",
        contrastText: "#ffffff",
    },
    secondary: {
        main: "#f57f17",
        light: "#ffb04c",
        dark: "#bc5100",
        contrastText: "#000000",
    },
    background: {
        default: "#f5f5f5",
        paper: "#ffffff",
    },
    text: {
        primary: "rgba(0, 0, 0, 0.87)",
        secondary: "rgba(0, 0, 0, 0.6)",
        disabled: "rgba(0, 0, 0, 0.38)",
    },
    grey: {
        300: "#e0e0e0",
        500: "#9e9e9e",
    },
    error: {
        main: '#d32f2f',
    },
};

export const layout = {
    containerMaxWidth: "lg",
    headerHeight: "64px",
    footerHeight: "auto",
};

export const commonStyles = {
    flexRowCenter: {
        display: "flex",
        flexDirection: "row",
        alignItems: "center",
        justifyContent: "center",
    },
    flexColumnCenter: {
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        justifyContent: "center",
    },
    link: {
        textDecoration: "none",
        color: "inherit",
        "&:hover": {},
    },
};
